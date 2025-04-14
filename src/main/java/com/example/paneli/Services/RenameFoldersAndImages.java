//package com.example.paneli.Services;
//
//import com.example.paneli.Models.HotelPhoto;
//import com.example.paneli.Repositories.HotelPhotoRepository;
//import com.example.paneli.Repositories.PropertyRepository;
//import com.example.paneli.Services.Mail.JavaMailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Service;
//
//import javax.mail.MessagingException;
//import java.io.IOException;
//import java.nio.file.*;
//import java.util.*;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//@Service
//public class RenameFoldersAndImages {
//
//    private final Set<HotelPhoto> finalHotelPhotoSet = new HashSet<>();
//
//    @Autowired
//    PropertyRepository propertyRepository;
//    @Autowired
//    HotelPhotoRepository hotelPhotoRepository;
//    @Autowired
//    JavaMailService javaMailService;
//
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void renameFoldersAndImages() throws MessagingException, IOException {
//
//        javaMailService.sendStartNotificationEmail(new Date());
//
//        long startTime = System.nanoTime();
//
//        System.out.println();
//        System.out.println("Rename-----------------------------------------------------------------------");
//        System.out.println();
//
//        finalHotelPhotoSet.clear();
//
//        Path directory = Paths.get("/home/allbookersusr/home/BookersDesk/data/klienti/");
//
//        if (!Files.exists(directory)) {
//            Files.createDirectories(directory);
//        }
//
//        // Iterate through each folder
//        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
//            for (Path folder : stream) {
//                try {
//                    if (Files.isDirectory(folder)) {
//
//                        System.out.println();
//                        String oldFolderName = folder.getFileName().toString();
//                        System.out.println("Old folder name --> " + oldFolderName);
//                        String newFolderName = renameFolder(oldFolderName);
//                        if (newFolderName != null) {
//
//                            System.out.println("New folder name --> " + newFolderName);
//                            System.out.println();
//                            Path renamedFolder = folder.resolveSibling(newFolderName);
//
//                            // Rename the folder
//                            if (Files.exists(renamedFolder) && Files.isDirectory(renamedFolder) && !Files.isSameFile(folder, renamedFolder)) {
//                                // Move contents to existing folder
//                                Files.list(folder).forEach(file -> {
//                                    try {
//                                        Files.move(file, renamedFolder.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                });
//
//                                // Delete old folder if it's empty and not the same as renamedFolder
//                                try {
//                                    Files.deleteIfExists(folder);
//                                } catch (DirectoryNotEmptyException e) {
//                                    System.out.println("Directory is not empty, cannot delete");
//                                }
//                            } else {
//                                // Rename the folder
//                                Files.move(folder, renamedFolder, StandardCopyOption.REPLACE_EXISTING);
//                            }
//
//                            // Rename images inside the folder
//                            Set<HotelPhoto> hotelPhotoSet = renameImagesInFolder(renamedFolder);
//                            if (!hotelPhotoSet.isEmpty()) {
//                                finalHotelPhotoSet.addAll(hotelPhotoSet);
//                            }
//                            System.out.println();
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            System.out.println("Size of photos to be saved --> " + finalHotelPhotoSet.size());
//            hotelPhotoRepository.saveAll(finalHotelPhotoSet);
//            System.out.println("Folders And Images Renamed Successfully!");
//            System.out.println("Time taken: " + ((System.nanoTime() - startTime) / 1_000_000_000.0) + " seconds");
//
//            javaMailService.sendEndNotificationEmail(new Date());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private Set<HotelPhoto> renameImagesInFolder(Path folderPath) throws IOException {
//
//        Set<HotelPhoto> hotelPhotoSet = new HashSet<>();
//
//        try {
//            String stringPropertyId = extractLastNumber(folderPath.getFileName().toString());
//            if (stringPropertyId != null) {
//                try {
//                    Long propertyId = Long.valueOf(stringPropertyId) - 2654435l;
//                    Map<String, List<HotelPhoto>> hotelPhotoMap = new HashMap<>();
//                    for (HotelPhoto hotelPhoto : hotelPhotoRepository.findAllByPropertyId(propertyId)) {
//                        hotelPhotoMap.computeIfAbsent(hotelPhoto.getFile_name(), k -> new ArrayList<>()).add(hotelPhoto);
//                    }
//                    try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath)) {
//                        for (Path file : stream) {
//                            try {
//                                if (Files.isRegularFile(file)) {
//
//                                    List<HotelPhoto> hotelPhotoList = hotelPhotoMap.get(file.getFileName().toString());
//                                    if (hotelPhotoList != null && !hotelPhotoList.isEmpty()) {
//                                        for (HotelPhoto hotelPhoto : hotelPhotoList) {
//
//                                            String oldFileName = file.getFileName().toString();
//                                            System.out.println("Old image name --> " + oldFileName);
//                                            String newFileName = renameImage(oldFileName, hotelPhoto);
//                                            if (newFileName != null) {
//
//                                                System.out.println("New image name --> " + newFileName);
//                                                Path newFilePath = file.resolveSibling(newFileName);
//
//                                                // Rename the image file
//                                                Files.move(file, newFilePath, StandardCopyOption.REPLACE_EXISTING);
//
//                                                hotelPhoto.setFile_name(newFileName);
//                                                hotelPhotoSet.add(hotelPhoto);
//                                            }
//                                        }
//                                    }
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            return hotelPhotoSet;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return hotelPhotoSet;
//        }
//    }
//
//    private String renameFolder(String oldFolderName) {
//
//        try {
//            String lastNumber = extractLastNumber(oldFolderName);
//            if (lastNumber != null) {
//                return "property_id_" + lastNumber;
//            } else {
//                return null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private String renameImage(String oldFileName, HotelPhoto hotelPhoto) {
//
//        try {
//            String fileExtension = getFileExtension(oldFileName);
//            if (fileExtension != null) {
//
//                String newImageName = "img_id_" + hotelPhoto.getId().toString() + fileExtension;
//                return newImageName;
//            } else {
//                return null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public String extractLastNumber(String input) {
//
//        try {
//            // Regular expression to find the last number in the string
//            Pattern pattern = Pattern.compile("\\d+$");
//            Matcher matcher = pattern.matcher(input);
//            if (matcher.find()) {
//
//                String numberStr = matcher.group(); // Get the matched number as a string
//                return numberStr;
//            } else {
//                return null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private String getFileExtension(String fileName) {
//
//        try {
//            int dotIndex = fileName.lastIndexOf('.');
//            return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
//        } catch (Exception e) {
//            return null;
//        }
//    }
//}
//
