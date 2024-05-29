import org.json.JSONArray;

public class StringSplit {
    public static void main(String args[]){
        String groupedTestcaseIDS = "DCEE_Inventory_1336606, DCEE_Inventory_1336299";
        String groupedTestcaseIDSA[] = groupedTestcaseIDS.split(",");

        // Trim each element to remove leading and trailing spaces
        for (int i = 0; i < groupedTestcaseIDSA.length; i++) {
            groupedTestcaseIDSA[i] = groupedTestcaseIDSA[i].trim();
        }

        JSONArray GroupedTestcaseIDSArray = new JSONArray(groupedTestcaseIDSA);
        System.out.println(GroupedTestcaseIDSArray);
        System.out.println(GroupedTestcaseIDSArray.length());
    }
}
