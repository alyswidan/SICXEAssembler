package com.systems.programming.assembler;

import com.systems.programming.assembler.Exceptions.AssemblerException;

import java.io.*;

/**
 * Created by ADMIN on 3/31/2017.
 */
public class Pass1 {

    private static int counter = 0;

    public static void execute() {
        PrintWriter clearer;
        if(Assembler.getCurrStart()==0)
        {
            try {
                clear(Assembler.getHTMEPath());
                clear(Assembler.getIntermediatePath());
                clear(Assembler.getSymTablePath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        System.out.println("=================pass1================");
        try (LineNumberReader sourceReader = new LineNumberReader(new FileReader(Assembler.getInputPath()));
             PrintWriter intermediateFileWrite = new PrintWriter(new FileWriter(Assembler.getIntermediatePath(),true),true);
             PrintWriter symTableWrite = new PrintWriter(new FileWriter(Assembler.getSymTablePath(),true),true)) {

            String line;

            // TODO: 18/05/17  sources reader .setLineNumnber
            for (int i = 0; i < Assembler.getCurrStart(); i++) {
                sourceReader.readLine();
            }
            LineParser.getInstance().setMode(LineParser.Mode.SHALLOW);
            while ((line = sourceReader.readLine()) != null) {
                try {
                    Line parsedLine = LineParser.getInstance().parse(line);
                    System.out.println("parsedLine = " + parsedLine);

                    if (parsedLine.isAssemblerExecutable())
                        parsedLine.execute();

                    if (parsedLine.getLabel() != null && !parsedLine.isEqu()
                            && (!parsedLine.isCSECT() && sourceReader.getLineNumber()!=Assembler.getCurrStart())) {
                        System.out.println("entered in mneeijjeoix " + parsedLine.getMnemonic());
                        SymTab.getInstance().putFull(parsedLine.getLabel(), Assembler.getLocationCounter(), SymTab.Type.RELATIVE, Assembler.getProgName());
                    }

                    if (parsedLine.isComment()) intermediateFileWrite.println(parsedLine.getComment());

                    else if (parsedLine.isCSECT() && sourceReader.getLineNumber() !=Assembler.getCurrStart()) {
                        Assembler.setNextProgName(parsedLine.getLabel());
                        Assembler.setProglength(Assembler.getLocationCounter());
                        System.out.println("PASS 1 exited <><><><><><><><><><><><><><><><><><><>");
                        //write address in first column
                        intermediateFileWrite.printf("0000");
                        //write the parsedLine
                        intermediateFileWrite.println(parsedLine);

                        Assembler.IncrementLocationCounterBy(parsedLine.getLength());
                        //setting the skipper value with the wanted number of skipped line
                        System.out.println("csect->"+parsedLine.getLabel()+" "+sourceReader.getLineNumber() );
                        Assembler.setNextStart(sourceReader.getLineNumber());
                        //Appending the SYMTAB File with the previous SYMTAB before the CSECT
                        symTableWrite.append(SymTab.getInstance().toString());
                        return;
                    }  else if (!parsedLine.isEmpty() && !parsedLine.isEnd()) {
                        //write address in first column
                        intermediateFileWrite.printf("%04X", Assembler.getLocationCounter());
                        //write the parsedLine
                        intermediateFileWrite.println(parsedLine);
                        Assembler.IncrementLocationCounterBy(parsedLine.getLength());
                    }  else if(parsedLine.isEmpty()) {
                        intermediateFileWrite.append(".\n");
                    }  else if(parsedLine.isEnd()){
                        //Assembler.setNextStart(sourceReader.getLineNumber());
                        //write address in first column
                        Assembler.setLastSect(true);
                        intermediateFileWrite.printf("%04X", Assembler.getLocationCounter());
                        //write the parsedLine
                        intermediateFileWrite.println(parsedLine);
                        Assembler.IncrementLocationCounterBy(parsedLine.getLength());
                        symTableWrite.append(SymTab.getInstance().toString());
                    }

                } catch (AssemblerException e) {
                    intermediateFileWrite.println(line.trim());
                    e.printStackTrace();
                    intermediateFileWrite.println("**Error^^:" + e.getClass().getSimpleName());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void clear(String path) throws FileNotFoundException {
        PrintWriter clearer;
        clearer = new PrintWriter(path);
        clearer.flush();
        clearer.close();
    }

}
