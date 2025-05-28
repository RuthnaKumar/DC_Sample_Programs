import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class OptimizedJsonNodeRemover {
    public static void main(String[] args) {
        // Sample JSON with single-level children
        String jsonData = "{\n" +
                "    \"headers\": [\n" +
                "        {\n" +
                "            \"displayName\": \"Computer Name\",\n" +
                "            \"format\": \"String Only\",\n" +
                "            \"isSearchEnabled\": true,\n" +
                "            \"canAutoResize\": true,\n" +
                "            \"errorMsg\": \"Please enter value in proper format as shown below\",\n" +
                "            \"validateMethod\": \"isStringSet\",\n" +
                "            \"sqlType\": \"CHAR\",\n" +
                "            \"savedWidth\": -1,\n" +
                "            \"tableCellView\": \"inventory/computers/view-components/computer-link-cell\",\n" +
                "            \"colIndex\": 0,\n" +
                "            \"columnCss\": \"sortedColumn\",\n" +
                "            \"disabled\": false,\n" +
                "            \"sortEnabled\": true,\n" +
                "            \"columnName\": \"Resource.NAME\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"rowSelection\": \"none\",\n" +
                "    \"reqParams\": \"toolID=1110\",\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"Resource.NAME.value\": \"AaronDowell\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Resource.NAME.value\": \"AubreyJenkins\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"isExportEnabled\": true,\n" +
                "    \"showHeader\": true,\n" +
                "    \"numFixedColumns\": 0,\n" +
                "    \"isAdvancedSearch\": false,\n" +
                "    \"showNavig\": false,\n" +
                "    \"navigation\": {\n" +
                "        \"startLinkIndex\": 1,\n" +
                "        \"showNextPage\": true,\n" +
                "        \"hasPaginationBottom\": true,\n" +
                "        \"range\": [\n" +
                "            25,\n" +
                "            50,\n" +
                "            75,\n" +
                "            100,\n" +
                "            200,\n" +
                "            300,\n" +
                "            400,\n" +
                "            500\n" +
                "        ],\n" +
                "        \"isNoCount\": false,\n" +
                "        \"type\": \"SELECT\",\n" +
                "        \"endLinkIndex\": 5,\n" +
                "        \"showFirstPage\": false,\n" +
                "        \"total\": 399,\n" +
                "        \"pages\": 16,\n" +
                "        \"hasPaginationTop\": true,\n" +
                "        \"itemsPerPage\": 25,\n" +
                "        \"from\": 1,\n" +
                "        \"to\": 25,\n" +
                "        \"prevPageIndex\": -24,\n" +
                "        \"showLastPage\": true,\n" +
                "        \"currentPage\": 1,\n" +
                "        \"showPrevPage\": false\n" +
                "    },\n" +
                "    \"templateName\": \"tableTemplate\",\n" +
                "    \"colList\": [\n" +
                "        {\n" +
                "            \"isChoosable\": true,\n" +
                "            \"display\": \"Computer Name\",\n" +
                "            \"name\": \"Resource.NAME\",\n" +
                "            \"isSortable\": false,\n" +
                "            \"isVisible\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"isChoosable\": true,\n" +
                "            \"display\": \"Device Friendly Name\",\n" +
                "            \"name\": \"ManagedComputer.FRIENDLY_NAME\",\n" +
                "            \"isSortable\": true,\n" +
                "            \"isVisible\": false\n" +
                "        }\n" +
                "    ],\n" +
                "    \"SQLTable\": false,\n" +
                "    \"isSearchPresent\": false,\n" +
                "    \"rowHover\": false,\n" +
                "    \"sortOrder\": true,\n" +
                "    \"name\": \"InvComputersByOS\",\n" +
                "    \"isScrollTable\": false,\n" +
                "    \"sortBy\": \"Resource.NAME\",\n" +
                "    \"noRowMsg\": \"No data available\",\n" +
                "    \"TableModel\": {\n" +
                "        \"tableModelRows\": [\n" +
                "            {\n" +
                "                \"ManagedComputer.FULL_NAME\": \"AARONDOWELL\",\n" +
                "                \"Resource.RESOURCE_ID\": \"16245000001133045\",\n" +
                "                \"Resource.NAME\": \"AaronDowell\",\n" +
                "                \"Resource.DOMAIN_NETBIOS_NAME\": \"UEMS\",\n" +
                "                \"BranchOfficeDetails.BRANCH_OFFICE_ID\": \"16245000000244874\",\n" +
                "                \"BranchOfficeDetails.BRANCH_OFFICE_NAME\": \"Manggar\",\n" +
                "                \"InvComputer.BOOT_UP_STATE\": \"Normal boot\",\n" +
                "                \"InvComputer.DESCRIPTION\": \"-\",\n" +
                "                \"InvComputer.MODEL\": \"Latitude E7450\",\n" +
                "                \"InvComputer.NO_OF_PROCESSORS\": \"1\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"viewColumns\": [\n" +
                "            \"Resource.NAME\"\n" +
                "        ],\n" +
                "        \"columnNames\": [\n" +
                "            \"ManagedComputer.FULL_NAME\",\n" +
                "            \"Resource.RESOURCE_ID\",\n" +
                "            \"Resource.NAME\",\n" +
                "            \"Resource.DOMAIN_NETBIOS_NAME\",\n" +
                "            \"BranchOfficeDetails.BRANCH_OFFICE_ID\",\n" +
                "            \"Computer.PROCESSOR_ARCHITECTURE\"\n" +
                "        ],\n" +
                "        \"rowSelectionType\": \"NONE\",\n" +
                "        \"uniqueId\": \"InvComputersByOS\"\n" +
                "    }\n" +
                "}";
        String nodeToRemove = "TableModel.tableModelRows"; // Node to remove

        try {
            ObjectMapper mapper = new ObjectMapper();

            // Parse JSON directly into ObjectNode since we know it's an object
            ObjectNode rootNode = (ObjectNode) mapper.readTree(jsonData);

            System.out.println("Original JSON:");
            //System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode));

            // Direct removal - no need for path parsing
            if (rootNode.has(nodeToRemove)) {
                rootNode.remove(nodeToRemove);
                System.out.println("\nRemoved node: " + nodeToRemove);
            } else {
                System.out.println("\nNode not found: " + nodeToRemove);
            }

            System.out.println("\nModified JSON:");
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}