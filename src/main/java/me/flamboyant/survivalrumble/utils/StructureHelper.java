package me.flamboyant.survivalrumble.utils;

import com.flowpowered.nbt.Tag;
import com.flowpowered.nbt.stream.NBTInputStream;
import com.flowpowered.nbt.stream.NBTOutputStream;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;

import java.io.*;
import java.util.Random;

public class StructureHelper {
    public static void spawnStructure(String structureName, Location location) {
        try {
            StructureManager mng = Bukkit.getStructureManager();
            //Structure mcStruct = mng.loadStructure(structureFile);
            /* System.out.println("Toto");
            NBTInputStream input = new NBTInputStream(Common.plugin.getResource(structureName + ".nbt"));
            */
            File targetFile = new File(System.getProperty("user.dir") + "/world/generated/minecraft/structures/" + structureName +".nbt");
            /* Tag tag = input.readTag();
            System.out.println("Type : " + tag.getType());
            OutputStream outStream = new FileOutputStream(targetFile);
            NBTOutputStream output = new NBTOutputStream(outStream);
            output.writeTag(tag);
            output.flush();
            outStream.flush();
            output.close();
            outStream.close();
            input.close();*/

            Structure mcStruct = mng.loadStructure(targetFile);

            mcStruct.place(location, true, StructureRotation.NONE, Mirror.NONE, 0, 1, new Random());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
