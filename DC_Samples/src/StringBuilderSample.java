public class StringBuilderSample {
    public static void main(String args[]){
        StringBuilder builder = new StringBuilder();
        String ppm_urls=" ";
        int i = 0;
        System.out.println("test");
        while (i<2) {
            String ppm_url = "test";
            builder.append(ppm_url);
            builder.append(", ");
            i++;
        }
        if (builder.length() > 0) {
            builder.delete(builder.length() - 2, builder.length());
        }
        ppm_urls = builder.toString();
        System.out.println(ppm_urls);
        System.out.println(ppm_urls.split(",").length);
    }
}
