/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapay_zeka_final_proje;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author cihan
 */
public class Yapay_Zeka_Final_Proje extends JApplet{
    private static BufferedImage img;
    private static int[][] img_array;
    private static int[][] mean_blue;
    private static int[][] mean_green;
    private static int[][] mean_red;
    private static int indiv_num = 20;
    private static int box_length = 15;
    private static int num_of_iterations = 35000;
    private static double mutation_rate;
    private static JLabel label;
    private static JFrame frame;
    private static JLabel label_2;
    private static JFrame frame_2; 
    private static Birey best;
    private static long max_dist = 0;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            String imgStr = "C:\\Users\\cihan\\Desktop\\Monalisa.jpg";
            Scanner scanner = new Scanner(System.in);
            System.out.println("Lütfen Bir Jenerasyondaki Toplam Birey Sayısını Giriniz.");
            indiv_num = scanner.nextInt();
            System.out.println("Lütfen Görüntüyü Oluşturmak İçin Kullanılacak Piksel Bloğunun Kenar Uzunluğunu Giriniz.");
            box_length = scanner.nextInt();
            System.out.println("Lütfen Toplam İterasyon Sayısını Giriniz.");
            num_of_iterations = scanner.nextInt();
            System.out.println("Lütfen Mutasyon Oranını Giriniz.");
            mutation_rate = scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Lütfen Programda Kullanmak İstediğiniz Resmin Adresini Giriniz.");
            imgStr = scanner.nextLine();
            
            img = ImageIO.read(new File(imgStr));
            img_array = new int[img.getWidth()][img.getHeight()];
            mean_blue = new int[(int)Math.ceil((double)img.getWidth() / (double)box_length)][(int)Math.ceil((double)img.getHeight() / (double)box_length)];
            mean_red = new int[(int)Math.ceil((double)img.getWidth() / (double)box_length)][(int)Math.ceil((double)img.getHeight() / (double)box_length)];
            mean_green = new int[(int)Math.ceil((double)img.getWidth() / (double)box_length)][(int)Math.ceil((double)img.getHeight() / (double)box_length)];
            //mutation_rate = Math.pow((double)box_length / ((double)img.getWidth() * (double)img.getHeight()), 0.9); Denenen en iyi mutasyon orani
            
            //resim n*n bloklara bolundugunde her bloga denk gelen ortalama rgb degerleri hesaplaniyor
            for(int i = 0; i < img.getWidth(); i++){
                for(int j = 0; j < img.getHeight(); j++){
                    img_array[i][j] = img.getRGB(i,j);  
                    mean_blue[i / box_length][j / box_length] += (img_array[i][j] & 0xFF);
                    mean_green[i / box_length][j / box_length] += ((img_array[i][j] >> 8) & 0xFF);
                    mean_red[i / box_length][j / box_length] += ((img_array[i][j] >> 16) & 0xFF);                  
                }
            }
            for(int i = 0; i < mean_blue.length; i++){
                for(int j = 0; j < mean_blue[0].length; j++){
                    if(i < mean_blue.length - 1 && j < mean_blue[0].length - 1 || (i == mean_blue.length - 1 && j != mean_blue[0].length - 1 && img.getWidth() % box_length == 0) || (i != mean_blue.length - 1 && j == mean_blue[0].length - 1 && img.getHeight() % box_length == 0) || (i == mean_blue.length - 1 && j == mean_blue[0].length - 1 && img.getWidth() % box_length == 0 && img.getHeight() % box_length == 0)){
                        mean_blue[i][j] = (mean_blue[i][j]/(box_length * box_length)); 
                        mean_green[i][j] = (mean_green[i][j]/(box_length * box_length)); 
                        mean_red[i][j] = (mean_red[i][j]/(box_length * box_length)); 
                        max_dist += (long)Math.max(mean_blue[i][j], 255 - mean_blue[i][j]);
                        max_dist += (long)Math.max(mean_green[i][j], 255 - mean_green[i][j]);
                        max_dist += (long)Math.max(mean_red[i][j], 255 - mean_red[i][j]);
                        continue;
                    }
                    if(i == mean_blue.length - 1 && j == mean_blue[0].length - 1 && img.getWidth() % box_length != 0 && img.getHeight() % box_length != 0){
                        mean_blue[i][j] = (mean_blue[i][j]/((img.getWidth() % box_length) * (img.getHeight() % box_length)));
                        mean_green[i][j] = (mean_green[i][j]/((img.getWidth() % box_length) * (img.getHeight() % box_length)));
                        mean_red[i][j] = (mean_red[i][j]/((img.getWidth() % box_length) * (img.getHeight() % box_length)));
                        max_dist += (long)Math.max(mean_blue[i][j], 255 - mean_blue[i][j]);
                        max_dist += (long)Math.max(mean_green[i][j], 255 - mean_green[i][j]);
                        max_dist += (long)Math.max(mean_red[i][j], 255 - mean_red[i][j]);
                        continue;
                    }
                    if(i == mean_blue.length - 1 && img.getWidth() % box_length != 0){
                        mean_blue[i][j] = (mean_blue[i][j]/((img.getWidth() % box_length) * box_length));
                        mean_green[i][j] = (mean_green[i][j]/((img.getWidth() % box_length) * box_length));
                        mean_red[i][j] = (mean_red[i][j]/((img.getWidth() % box_length) * box_length));
                        max_dist += (long)Math.max(mean_blue[i][j], 255 - mean_blue[i][j]);
                        max_dist += (long)Math.max(mean_green[i][j], 255 - mean_green[i][j]);
                        max_dist += (long)Math.max(mean_red[i][j], 255 - mean_red[i][j]);
                        continue;
                    }
                    if(j == mean_blue[0].length - 1 && img.getHeight() % box_length != 0){
                        mean_blue[i][j] = (mean_blue[i][j]/((img.getHeight() % box_length) * box_length));
                        mean_green[i][j] = (mean_green[i][j]/((img.getHeight() % box_length) * box_length));
                        mean_red[i][j] = (mean_red[i][j]/((img.getHeight() % box_length) * box_length));
                        max_dist += (long)Math.max(mean_blue[i][j], 255 - mean_blue[i][j]);
                        max_dist += (long)Math.max(mean_green[i][j], 255 - mean_green[i][j]);
                        max_dist += (long)Math.max(mean_red[i][j], 255 - mean_red[i][j]);
                    }
                }
            }
            
            Birey b[] = new Birey[indiv_num];
            for(int i = 0; i < indiv_num; i++){
                b[i] = new Birey(img.getWidth(), img.getHeight());
            }
            
            //bireyler fitness degerine gore siralaniyor ve butun bireylerin fitness degerinden en uyumsuz bireyin fitness degeri cikariliyor.
            Arrays.sort(b, new Comparator<Birey>(){
                @Override
                public int compare(Birey b1 ,Birey b2){
                    return -(((Long)b1.get_fitness()).compareTo((Long)b2.get_fitness())); //buyukten kucuge siralama
                }
            });
            for(int j = 0; j < indiv_num; j++){
                    b[j].set_fitness(b[j].get_fitness() - b[indiv_num - 1].get_fitness());
            }            
            set_the_pane();
            best = b[0];
            
            for(int i = 0; i < num_of_iterations; i++){
                //yeni bireyler olustur
                Birey new_indiv[] = new Birey[indiv_num];
                             
                long fitness_array[] = new long[indiv_num];
                fitness_array[0] = b[0].get_fitness();
                
                for(int j = 1; j < indiv_num; j++){
                    fitness_array[j] = fitness_array[j-1] + b[j].get_fitness();
                }               
                
                for(int l = 0; l < indiv_num; l++){
                     //Selection
                    long rand1 = (long)(Math.random() * (double)fitness_array[indiv_num - 1]);
                    long rand2 = (long)(Math.random() * (double)fitness_array[indiv_num - 1]);

                    Birey b1 = null;
                    Birey b2 = null;

                    for(int j = 0; j < indiv_num; j++){
                        if(rand1 <= fitness_array[j]){
                            b1 = b[j];                            
                            break;
                        }                    
                    }

                    for(int j = 0; j < indiv_num; j++){
                        if(rand2 <= fitness_array[j]){
                            b2 = b[j];
                            break;
                        }                    
                    }
                    
                    //Uniform Cross-over
                    int map[][] = new int[(int)Math.ceil((double)img.getWidth() / (double)box_length)][(int)Math.ceil((double)img.getHeight() / (double)box_length)];
                    int pixel[][] = new int[img.getWidth()][img.getHeight()];
                    for(int j = 0; j < map.length; j++){
                        for(int k = 0; k < map[0].length; k++){
                            map[j][k] = (int)(Math.random() * 2.0);
                        }
                    }

                    for(int j = 0; j < map.length; j++){
                        for(int k = 0; k < map[0].length; k++){
                            if(map[j][k] == 0){
                                pixel[box_length*j][box_length*k] = b1.get_pixel(box_length*j, box_length*k);
                                for(int p = 0; p < box_length && box_length*j + p < img.getWidth(); p++){
                                    for(int o = 0; o < box_length && box_length*k + o < img.getHeight(); o++){
                                        pixel[box_length*j + p][box_length*k + o] = pixel[box_length*j][box_length*k];
                                    }
                                }
                            }
                            else{
                                pixel[box_length*j][box_length*k] = b2.get_pixel(box_length*j, box_length*k);
                                for(int p = 0; p < box_length && box_length*j + p < img.getWidth(); p++){
                                    for(int o = 0; o < box_length && box_length*k + o < img.getHeight(); o++){
                                        pixel[box_length*j + p][box_length*k + o] = pixel[box_length*j][box_length*k];
                                    }
                                }
                            }
                        }
                    }

                    //Mutation
                    for(int j = 0; j < map.length; j++){
                        for(int k = 0; k < map[0].length; k++){
                            if(Math.random() <= mutation_rate){
                                pixel[box_length*j][box_length*k] = (int)(Math.random() * 16777216.0);
                                for(int p = 0; p < box_length && box_length*j + p < img.getWidth(); p++){
                                    for(int o = 0; o < box_length && box_length*k + o < img.getHeight(); o++){
                                        pixel[box_length*j + p][box_length*k + o] = pixel[box_length*j][box_length*k];
                                    }
                                }
                            }
                        }
                    }
                    new_indiv[l] = new Birey(img.getWidth(), img.getHeight(), pixel);
                }
                b = new_indiv;
                
                //bireyler fitness degerine gore siralaniyor ve butun bireylerin fitness degerinden en uyumsuz bireyin fitness degeri cikariliyor.
                Arrays.sort(b, new Comparator<Birey>(){
                    @Override
                    public int compare(Birey b1 ,Birey b2){
                        return -(((Long)b1.get_fitness()).compareTo((Long)b2.get_fitness())); //buyukten kucuge siralama
                    }
                });
                for(int j = 0; j < indiv_num; j++){
                    b[j].set_fitness(b[j].get_fitness() - b[indiv_num - 1].get_fitness());
                }
                if(i % 1000 == 0)
                    System.out.println("\n" + i + ". iterasyon, jenerasyondaki en iyi bireyin başarı oranı: " + b[0].get_success());
                //jenerasyonun en iyi bireyi ekranda gosteriliyor.
                print(b[0].indiv_img);         
                if(b[0].get_success() > best.get_success())
                    best = b[0];
            }
            //olusturulan en basarili birey ekranda gosteriliyor.
            print(best.indiv_img);        
            System.out.println("Oluşturulan en iyi bireyin başarı oranı: " + best.get_success());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    
    private static void set_the_pane(){
        BufferedImage print_img = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);    
    
        for(int y = 0; y < img.getHeight(); y++){
            for(int x = 0; x < img.getWidth(); x++){
                print_img.setRGB(x, y, 0);      
            }
        }
        frame = new JFrame("Cikti");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        label = new JLabel();
        label.setIcon(new ImageIcon(print_img));
        frame.getContentPane().add(label);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        frame_2 = new JFrame("Orijinal Resim");
        frame_2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        label_2 = new JLabel();
        label_2.setIcon(new ImageIcon(img));
        frame_2.getContentPane().add(label_2);
        frame_2.pack();
        frame_2.setLocationRelativeTo(null);
        frame_2.setVisible(true);
    }
    
    private static void print(int pixel[][]){
        BufferedImage print_img = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);    
        for(int y = 0; y < img.getHeight(); y++){
            for(int x = 0; x < img.getWidth(); x++){
                print_img.setRGB(x, y, pixel[x][y]);      
            }
        }       
        label.setIcon(new ImageIcon(print_img));
        frame.pack();
    }
    
    static class Birey{
        private int height;
        private int width;
        private int indiv_img[][];
        private long fitness_val;
        private double success;
        
        public Birey(int width, int height){
            this.height = height;
            this.width = width;
            this.indiv_img = new int[width][height];
                     
            for(int i = 0; i < (int)Math.ceil((double)width / (double)box_length); i++){
                for(int j = 0; j < (int)Math.ceil((double)height / (double)box_length); j++){
                    this.indiv_img[box_length*i][box_length*j] = (int)(Math.random() * 16777216.0);
                    for(int p = 0; p < box_length && box_length*i + p < width; p++){
                        for(int o = 0; o < box_length && box_length*j + o < height; o++){
                            this.indiv_img[box_length*i + p][box_length*j + o] = this.indiv_img[box_length*i][box_length*j];
                        }
                    }                    
                }
            }
            
            this.fitness_val = fitness();
        }
        
        public Birey(int width, int height, int array[][]){
            this.height = height;
            this.width = width;
            this.indiv_img = array;           
            this.fitness_val = fitness();
        }
        
        public int get_pixel(int w, int h){
            return indiv_img[w][h];
        }
        
        public long get_fitness(){
            return fitness_val;
        }
        
        public void set_fitness(long f){
            fitness_val = f;
        }
 
        public double get_success(){
            return success;
        }
        
        //fitness fonksiyonu olusturulan bireydeki piksel bloklari ile asil resmin piksel bloklari arasindaki uzakligin negatifidir.
        public long fitness(){
            long sum = 0;
           
            for(int i = 0; i < (int)Math.ceil((double)width / (double)box_length); i++){
                for(int j = 0; j < (int)Math.ceil((double)height / (double)box_length); j++){
                    long blue = (long)Math.abs((double)(indiv_img[i*box_length][j*box_length] & 0xFF) - (double)(mean_blue[i][j] & 0xFF));
                    long green = (long)Math.abs((double)((indiv_img[i*box_length][j*box_length] >> 8) & 0xFF) - (double)(mean_green[i][j] & 0xFF));
                    long red = (long)Math.abs((double)((indiv_img[i*box_length][j*box_length] >> 16) & 0xFF) - (double)(mean_red[i][j] & 0xFF)); 
                    sum += blue;
                    sum += green;
                    sum += red;
                }    
            }
            //basari orani piksel blok degerlerinin orijinal resimdeki ortalama piksel blok degerlerine olan yakinliginin 0 ila 1 arasina normalize edilmis halidir.
            success = 1.0 - (double)sum / (double)max_dist;
            return -sum;
        }
    }
}
