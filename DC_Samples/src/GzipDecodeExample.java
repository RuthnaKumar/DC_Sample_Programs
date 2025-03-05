import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipDecodeExample {

    public static void main(String[] args) throws IOException {

        byte[] gzippedData = gzipJson("{\"headers\":[{\"displayName\":\"Name\",\"isSearchEnabled\":false,\"canAutoResize\":true,\"sqlType\":\"CHAR\",\"savedWidth\":-1,\"tableCellView\":\"configuration/templates/view-components/template-name-link-cell\",\"colIndex\":0,\"columnCss\":\"sortedColumn\",\"sqlTblColindex\":\"3\",\"disabled\":false,\"sortEnabled\":true,\"searchValue\":\"\",\"createMenuScript\":{\"linkParams\":\"\",\"menuDataProps\":{\"actionType\":\"GET\",\"displayName\":\"Configuration Template Details\",\"linkParams\":\"\",\"actionLink\":\"template.do?actionToCall=showTemplate\",\"target\":\"_self\"},\"menuItemId\":\"ConfigTemplateDetails\"},\"columnName\":\"template_name\"},{\"displayName\":\"Category\",\"isSearchEnabled\":false,\"canAutoResize\":true,\"sqlType\":\"BIGINT\",\"savedWidth\":-1,\"tableCellView\":\"configuration/templates/view-components/template-category-image-cell\",\"colIndex\":1,\"columnCss\":\"\",\"sqlTblColindex\":\"4\",\"disabled\":false,\"sortEnabled\":true,\"searchValue\":\"\",\"columnName\":\"Type\"},{\"sqlType\":\"CHAR\",\"savedWidth\":-1,\"displayName\":\"Description\",\"colIndex\":2,\"columnCss\":\"\",\"isSearchEnabled\":false,\"canAutoResize\":true,\"sqlTblColindex\":\"5\",\"disabled\":false,\"sortEnabled\":true,\"searchValue\":\"\",\"columnName\":\"template_desc\"},{\"displayName\":\"Action\",\"isSearchEnabled\":false,\"canAutoResize\":true,\"sqlType\":\"BIGINT\",\"savedWidth\":-1,\"tableCellView\":\"configuration/templates/pre-defined-templates/view-components/template-action-cell\",\"colIndex\":3,\"columnCss\":\"\",\"sqlTblColindex\":\"2\",\"disabled\":false,\"sortEnabled\":false,\"searchValue\":\"\",\"columnName\":\"collectionID\"}],\"rowSelection\":\"none\",\"reqParams\":\"\",\"data\":[{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":0,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#65&#117&#116&#111&#45&#76&#111&#99&#107&#105&#110&#103&#32&#80&#67&#115\"},{\"value\":\"&#50\"},{\"value\":\"&#67&#108&#105&#101&#110&#116&#32&#99&#111&#109&#112&#117&#116&#101&#114&#115&#32&#119&#105&#108&#108&#32&#98&#101&#32&#97&#117&#116&#111&#109&#97&#116&#105&#99&#97&#108&#108&#121&#32&#108&#111&#99&#107&#101&#100&#32&#98&#97&#115&#101&#100&#32&#111&#110&#32&#116&#104&#101&#32&#116&#105&#109&#101&#32&#105&#110&#116&#101&#114&#118&#97&#108&#32&#115&#112&#101&#99&#105&#102&#105&#101&#100&#32&#104&#101&#114&#101&#32&#40&#105&#110&#32&#115&#101&#99&#111&#110&#100&#115&#41\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#56&#50&#52&#50\"}],\"rowIdx\":0},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":1,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#66&#108&#111&#99&#107&#32&#69&#88&#69&#32&#40&#100&#101&#112&#114&#101&#99&#97&#116&#101&#100&#41\"},{\"value\":\"&#50\"},{\"value\":\"&#80&#114&#101&#118&#101&#110&#116&#32&#117&#115&#101&#114&#115&#32&#102&#114&#111&#109&#32&#114&#117&#110&#110&#105&#110&#103&#32&#115&#112&#101&#99&#105&#102&#105&#99&#32&#69&#88&#69&#32&#102&#105&#108&#101&#115\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#57&#53&#49&#52\"}],\"rowIdx\":1},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":2,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#67&#104&#97&#110&#103&#101&#32&#68&#97&#116&#101&#32&#70&#111&#114&#109&#97&#116\"},{\"value\":\"&#50\"},{\"value\":\"&#67&#104&#97&#110&#103&#101&#32&#116&#104&#101&#32&#115&#121&#115&#116&#101&#109&#32&#100&#97&#116&#101&#32&#102&#111&#114&#109&#97&#116&#32&#116&#111&#32&#121&#121&#121&#121&#45&#77&#77&#45&#100&#100\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#56&#49&#51&#54\"}],\"rowIdx\":2},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":3,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#67&#104&#97&#110&#103&#101&#32&#76&#111&#99&#97&#108&#32&#65&#100&#109&#105&#110&#32&#65&#99&#99&#111&#117&#110&#116&#32&#80&#97&#115&#115&#119&#111&#114&#100\"},{\"value\":\"&#49\"},{\"value\":\"&#67&#104&#97&#110&#103&#101&#115&#32&#116&#104&#101&#32&#108&#111&#99&#97&#108&#32&#97&#100&#109&#105&#110&#105&#115&#116&#114&#97&#116&#111&#114&#115&#32&#112&#97&#115&#115&#119&#111&#114&#100\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#56&#50&#54&#50\"}],\"rowIdx\":3},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":4,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#67&#104&#97&#110&#103&#101&#32&#80&#111&#119&#101&#114&#32&#83&#99&#104&#101&#109&#101\"},{\"value\":\"&#49\"},{\"value\":\"&#67&#104&#97&#110&#103&#101&#32&#108&#97&#112&#116&#111&#112&#32&#112&#111&#119&#101&#114&#32&#115&#99&#104&#101&#109&#101&#115&#32&#116&#111&#32&#115&#97&#118&#101&#32&#112&#111&#119&#101&#114\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#56&#56&#55&#52\"}],\"rowIdx\":4},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":5,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#67&#104&#97&#110&#103&#101&#32&#84&#105&#109&#101&#32&#70&#111&#114&#109&#97&#116\"},{\"value\":\"&#50\"},{\"value\":\"&#67&#104&#97&#110&#103&#101&#32&#116&#104&#101&#32&#115&#121&#115&#116&#101&#109&#32&#116&#105&#109&#101&#32&#102&#111&#114&#109&#97&#116&#32&#102&#114&#111&#109&#32&#50&#52&#32&#104&#111&#117&#114&#115&#32&#116&#111&#32&#49&#50&#32&#104&#111&#117&#114&#115\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#56&#49&#57&#52\"}],\"rowIdx\":5},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":6,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#67&#108&#101&#97&#110&#117&#112&#32&#82&#101&#99&#121&#99&#108&#101&#32&#66&#105&#110\"},{\"value\":\"&#49\"},{\"value\":\"&#70&#114&#101&#101&#115&#32&#117&#112&#32&#116&#104&#101&#32&#104&#97&#114&#100&#32&#100&#105&#115&#107&#32&#115&#112&#97&#99&#101&#32&#98&#121&#32&#114&#101&#109&#111&#118&#105&#110&#103&#32&#116&#104&#101&#32&#117&#110&#119&#97&#110&#116&#101&#100&#32&#100&#97&#116&#97&#32&#102&#114&#111&#109&#32&#116&#104&#101&#32&#82&#101&#99&#121&#99&#108&#101&#32&#66&#105&#110\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#56&#50&#56&#56\"}],\"rowIdx\":6},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":7,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#67&#111&#110&#102&#105&#103&#117&#114&#101&#32&#79&#68&#66&#67&#32&#83&#101&#116&#116&#105&#110&#103&#115&#32&#70&#111&#114&#32&#83&#121&#115&#116&#101&#109\"},{\"value\":\"&#49\"},{\"value\":\"&#67&#111&#110&#102&#105&#103&#117&#114&#101&#115&#32&#116&#104&#101&#32&#79&#68&#66&#67&#32&#100&#97&#116&#97&#115&#111&#117&#114&#99&#101&#32&#110&#97&#109&#101&#32&#116&#111&#32&#101&#110&#97&#98&#108&#101&#32&#97&#108&#108&#32&#117&#115&#101&#114&#115&#32&#116&#111&#32&#99&#111&#110&#110&#101&#99&#116&#32&#116&#111&#32&#97&#32&#100&#97&#116&#97&#98&#97&#115&#101&#32&#117&#115&#105&#110&#103&#32&#79&#68&#66&#67\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#57&#51&#57&#54\"}],\"rowIdx\":7},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":8,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#67&#111&#110&#102&#105&#103&#117&#114&#101&#32&#79&#68&#66&#67&#32&#83&#101&#116&#116&#105&#110&#103&#115&#32&#70&#111&#114&#32&#85&#115&#101&#114\"},{\"value\":\"&#50\"},{\"value\":\"&#67&#111&#110&#102&#105&#103&#117&#114&#101&#115&#32&#116&#104&#101&#32&#79&#68&#66&#67&#32&#100&#97&#116&#97&#115&#111&#117&#114&#99&#101&#32&#110&#97&#109&#101&#32&#116&#111&#32&#101&#110&#97&#98&#108&#101&#32&#117&#115&#101&#114&#32&#116&#111&#32&#99&#111&#110&#110&#101&#99&#116&#32&#116&#111&#32&#97&#32&#100&#97&#116&#97&#98&#97&#115&#101&#32&#117&#115&#105&#110&#103&#32&#79&#68&#66&#67\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#57&#52&#51&#50\"}],\"rowIdx\":8},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":9,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#67&#114&#101&#97&#116&#101&#32&#65&#108&#116&#101&#114&#110&#97&#116&#101&#32&#76&#111&#99&#97&#108&#32&#65&#100&#109&#105&#110&#32&#65&#99&#99&#111&#117&#110&#116\"},{\"value\":\"&#49\"},{\"value\":\"&#67&#114&#101&#97&#116&#101&#115&#32&#97&#110&#32&#97&#108&#116&#101&#114&#110&#97&#116&#105&#118&#101&#32&#108&#111&#99&#97&#108&#32&#97&#100&#109&#105&#110&#105&#115&#116&#114&#97&#116&#105&#118&#101&#32&#117&#115&#101&#114&#32&#97&#99&#99&#111&#117&#110&#116\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#56&#52&#57&#56\"}],\"rowIdx\":9},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":10,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#68&#101&#102&#114&#97&#103&#32&#72&#97&#114&#100&#32&#68&#105&#115&#107\"},{\"value\":\"&#49\"},{\"value\":\"&#68&#101&#102&#114&#97&#103&#109&#101&#110&#116&#115&#32&#116&#104&#101&#32&#102&#114&#97&#103&#109&#101&#110&#116&#101&#100&#32&#104&#97&#114&#100&#32&#100&#105&#115&#107&#32&#97&#110&#100&#32&#105&#109&#112&#114&#111&#118&#101&#115&#32&#116&#104&#101&#32&#112&#101&#114&#102&#111&#114&#109&#97&#110&#99&#101\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#56&#53&#50&#56\"}],\"rowIdx\":10},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":11,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#68&#101&#108&#101&#116&#101&#32&#76&#111&#99&#97&#108&#32&#65&#100&#109&#105&#110&#32&#65&#99&#99&#111&#117&#110&#116\"},{\"value\":\"&#49\"},{\"value\":\"&#68&#101&#108&#101&#116&#101&#115&#32&#116&#104&#101&#32&#108&#111&#99&#97&#108&#32&#65&#100&#109&#105&#110&#105&#115&#116&#114&#97&#116&#111&#114&#32&#97&#99&#99&#111&#117&#110&#116&#32&#116&#111&#32&#101&#110&#104&#97&#110&#99&#101&#32&#115&#101&#99&#117&#114&#105&#116&#121\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#56&#53&#57&#52\"}],\"rowIdx\":11},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":12,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#32&#97&#99&#99&#101&#115&#115&#32&#116&#111&#32&#116&#104&#101&#32&#87&#105&#110&#100&#111&#119&#115&#32&#117&#112&#100&#97&#116&#101&#32&#102&#101&#97&#116&#117&#114&#101\"},{\"value\":\"&#50\"},{\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#115&#32&#117&#115&#101&#114&#32&#102&#114&#111&#109&#32&#97&#99&#99&#101&#115&#115&#105&#110&#103&#32&#116&#104&#101&#32&#87&#105&#110&#100&#111&#119&#115&#32&#117&#112&#100&#97&#116&#101&#32&#102&#101&#97&#116&#117&#114&#101\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#57&#53&#52&#48\"}],\"rowIdx\":12},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":13,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#32&#65&#100&#111&#98&#101&#32&#65&#99&#114&#111&#98&#97&#116&#32&#88&#32&#85&#112&#100&#97&#116&#101&#114\"},{\"value\":\"&#49\"},{\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#115&#32&#116&#104&#101&#32&#97&#117&#116&#111&#109&#97&#116&#105&#99&#32&#117&#112&#100&#97&#116&#101&#115&#32&#111&#102&#32&#65&#100&#111&#98&#101&#32&#65&#99&#114&#111&#98&#97&#116&#32&#88\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#57&#57&#51&#56\"}],\"rowIdx\":13},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":14,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#32&#65&#100&#111&#98&#101&#32&#65&#99&#114&#111&#98&#97&#116&#32&#88&#73&#32&#85&#112&#100&#97&#116&#101&#114\"},{\"value\":\"&#49\"},{\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#115&#32&#116&#104&#101&#32&#97&#117&#116&#111&#109&#97&#116&#105&#99&#32&#117&#112&#100&#97&#116&#101&#115&#32&#111&#102&#32&#65&#100&#111&#98&#101&#32&#65&#99&#114&#111&#98&#97&#116&#32&#88&#73\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#57&#57&#54&#48\"}],\"rowIdx\":14},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":15,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#32&#65&#100&#111&#98&#101&#32&#65&#99&#114&#111&#98&#97&#116&#32&#82&#101&#97&#100&#101&#114&#32&#68&#67&#32&#117&#112&#100&#97&#116&#101&#114\"},{\"value\":\"&#49\"},{\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#115&#32&#116&#104&#101&#32&#97&#117&#116&#111&#109&#97&#116&#105&#99&#32&#117&#112&#100&#97&#116&#101&#115&#32&#111&#102&#32&#65&#100&#111&#98&#101&#32&#65&#99&#114&#111&#98&#97&#116&#32&#82&#101&#97&#100&#101&#114&#32&#68&#67\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#57&#51&#54&#54\"}],\"rowIdx\":15},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":16,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#32&#65&#100&#111&#98&#101&#32&#65&#73&#82&#32&#85&#112&#100&#97&#116&#101&#114\"},{\"value\":\"&#49\"},{\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#115&#32&#116&#104&#101&#32&#97&#117&#116&#111&#109&#97&#116&#105&#99&#32&#117&#112&#100&#97&#116&#101&#115&#32&#111&#102&#32&#65&#100&#111&#98&#101&#32&#65&#73&#82&#32&#116&#104&#97&#116&#32&#105&#115&#32&#105&#110&#115&#116&#97&#108&#108&#101&#100&#32&#97&#116&#32&#116&#104&#101&#32&#115&#121&#115&#116&#101&#109&#32&#108&#101&#118&#101&#108\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#57&#51&#52&#48\"}],\"rowIdx\":16},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":17,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#32&#65&#100&#111&#98&#101&#32&#82&#101&#97&#100&#101&#114&#32&#49&#48&#32&#117&#112&#100&#97&#116&#101&#114\"},{\"value\":\"&#49\"},{\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#115&#32&#116&#104&#101&#32&#97&#117&#116&#111&#109&#97&#116&#105&#99&#32&#117&#112&#100&#97&#116&#101&#115&#32&#111&#102&#32&#65&#100&#111&#98&#101&#32&#82&#101&#97&#100&#101&#114&#32&#49&#48\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#57&#49&#57&#56\"}],\"rowIdx\":17},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":18,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#32&#65&#100&#111&#98&#101&#32&#82&#101&#97&#100&#101&#114&#32&#49&#49&#32&#117&#112&#100&#97&#116&#101&#114\"},{\"value\":\"&#49\"},{\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#115&#32&#116&#104&#101&#32&#97&#117&#116&#111&#109&#97&#116&#105&#99&#32&#117&#112&#100&#97&#116&#101&#115&#32&#111&#102&#32&#65&#100&#111&#98&#101&#32&#82&#101&#97&#100&#101&#114&#32&#49&#49\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#57&#50&#50&#52\"}],\"rowIdx\":18},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":19,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#32&#65&#100&#111&#98&#101&#32&#82&#101&#97&#100&#101&#114&#32&#56&#32&#117&#112&#100&#97&#116&#101&#114\"},{\"value\":\"&#49\"},{\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#115&#32&#116&#104&#101&#32&#97&#117&#116&#111&#109&#97&#116&#105&#99&#32&#117&#112&#100&#97&#116&#101&#115&#32&#111&#102&#32&#65&#100&#111&#98&#101&#32&#82&#101&#97&#100&#101&#114&#32&#56\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#57&#49&#48&#50\"}],\"rowIdx\":19},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":20,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#32&#65&#100&#111&#98&#101&#32&#82&#101&#97&#100&#101&#114&#32&#57&#32&#117&#112&#100&#97&#116&#101&#114\"},{\"value\":\"&#49\"},{\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#115&#32&#116&#104&#101&#32&#97&#117&#116&#111&#109&#97&#116&#105&#99&#32&#117&#112&#100&#97&#116&#101&#115&#32&#111&#102&#32&#65&#100&#111&#98&#101&#32&#82&#101&#97&#100&#101&#114&#32&#57\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#57&#49&#55&#50\"}],\"rowIdx\":20},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":21,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#32&#65&#100&#111&#98&#101&#32&#83&#104&#111&#99&#107&#119&#97&#118&#101&#32&#49&#49&#32&#117&#112&#100&#97&#116&#101&#114\"},{\"value\":\"&#49\"},{\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#115&#32&#116&#104&#101&#32&#97&#117&#116&#111&#109&#97&#116&#105&#99&#32&#117&#112&#100&#97&#116&#101&#115&#32&#111&#102&#32&#65&#100&#111&#98&#101&#32&#83&#104&#111&#99&#107&#119&#97&#118&#101&#32&#49&#49\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#57&#50&#53&#48\"}],\"rowIdx\":21},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":22,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#32&#65&#117&#116&#111&#32&#85&#112&#100&#97&#116&#101&#32&#111&#102&#32&#71&#111&#111&#103&#108&#101&#32&#67&#104&#114&#111&#109&#101\"},{\"value\":\"&#49\"},{\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#115&#32&#97&#117&#116&#111&#109&#97&#116&#105&#99&#32&#117&#112&#100&#97&#116&#101&#32&#111&#102&#32&#71&#111&#111&#103&#108&#101&#32&#67&#104&#114&#111&#109&#101\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#56&#49&#54&#52\"}],\"rowIdx\":22},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":23,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#32&#65&#117&#116&#111&#109&#97&#116&#105&#99&#32&#65&#112&#112&#32&#85&#112&#100&#97&#116&#101&#115\"},{\"value\":\"&#49\"},{\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#115&#32&#97&#117&#116&#111&#109&#97&#116&#105&#99&#32&#117&#112&#100&#97&#116&#101&#115&#32&#111&#102&#32&#119&#105&#110&#100&#111&#119&#115&#32&#97&#112&#112&#115&#46\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#57&#56&#57&#48\"}],\"rowIdx\":23},{\"cells\":[{\"action\":{\"menuInvoker\":{\"reqParams\":\"\",\"rowIndex\":24,\"menuItemId\":\"ConfigTemplateDetails\",\"uniqueId\":\"ConfigTemplateView\"},\"displayName\":\"Configuration Template Details\",\"menuItemId\":\"ConfigTemplateDetails\",\"emberActionName\":\"invokeMenuAction\"},\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#32&#65&#117&#116&#111&#109&#97&#116&#105&#99&#32&#68&#101&#108&#105&#118&#101&#114&#121&#32&#111&#102&#32&#73&#69&#32&#49&#48\"},{\"value\":\"&#49\"},{\"value\":\"&#68&#105&#115&#97&#98&#108&#101&#115&#32&#73&#110&#116&#101&#114&#110&#101&#116&#32&#69&#120&#112&#108&#111&#114&#101&#114&#32&#102&#114&#111&#109&#32&#117&#112&#103&#114&#97&#100&#105&#110&#103&#32&#116&#111&#32&#118&#101&#114&#115&#105&#111&#110&#32&#49&#48&#32&#116&#104&#114&#111&#117&#103&#104&#32&#87&#105&#110&#100&#111&#119&#115&#32&#117&#112&#100&#97&#116&#101&#115\"},{\"value\":\"&#57&#53&#48&#53&#48&#48&#48&#48&#48&#48&#48&#48&#57&#48&#51&#52\"}],\"rowIdx\":24}],\"isExportEnabled\":false,\"showHeader\":true,\"numFixedColumns\":0,\"isAdvancedSearch\":false,\"showNavig\":false,\"navigation\":{\"startLinkIndex\":1,\"showNextPage\":true,\"hasPaginationBottom\":true,\"range\":[25,50,75,100,200,300,400,500],\"isNoCount\":false,\"type\":\"SELECT\",\"endLinkIndex\":3,\"showFirstPage\":false,\"total\":74,\"pages\":3,\"hasPaginationTop\":true,\"itemsPerPage\":25,\"from\":1,\"to\":25,\"prevPageIndex\":-24,\"showLastPage\":true,\"currentPage\":1,\"showPrevPage\":false},\"templateName\":\"tableTemplate\",\"SQLTable\":true,\"isSearchPresent\":false,\"rowHover\":false,\"sortOrder\":true,\"name\":\"ConfigTemplateView\",\"isScrollTable\":false,\"sortBy\":\"template_name\",\"noRowMsg\":\"No templates are available\",\"TableModel\":{\"tableModelRows\":[[\"9505000000008251\",\"9505000000008242\",\"dc.conf.template.Auto-locking_PCs\",\"2\",\"dc.conf.template.Auto-locking_PCs_after_x-amount_of_idle_time\"],[\"9505000000009525\",\"9505000000009514\",\"dc.conf.template.Block_EXE\",\"2\",\"dc.conf.template.Prevent_users_from_running_specific_EXE_files\"],[\"9505000000008143\",\"9505000000008136\",\"dc.conf.template.Change_Date_Format_title\",\"2\",\"dc.conf.template.Change_Date_Format_desc\"],[\"9505000000008271\",\"9505000000008262\",\"dc.conf.template.Change_Local_Admin_Account_Password\",\"1\",\"dc.conf.template.Changes_the_local_admin_pwd\"],[\"9505000000008881\",\"9505000000008874\",\"dc.conf.template.Change_Power_Scheme\",\"1\",\"dc.conf.template.Change_laptop_power_schemes_to_save_power\"],[\"9505000000008203\",\"9505000000008194\",\"dc.conf.template.Change_Time_Format_title\",\"2\",\"dc.conf.template.Change_Time_Format_desc\"],[\"9505000000008432\",\"9505000000008288\",\"dc.conf.template.Cleanup_Recycle_Bin\",\"1\",\"dc.conf.template.Frees_up_the_hard_disk_space_by_removing_the_unwanted_data_from_the_Recycle_Bin\"],[\"9505000000009409\",\"9505000000009396\",\"dc.conf.template.Configure_ODBC_Settings_For_System\",\"1\",\"dc.conf.template.Configures_the_ODBC_datasource_name_to_enable_all_users_to_connect_to_a_database_using_ODBC\"],[\"9505000000009447\",\"9505000000009432\",\"dc.conf.template.Configure_ODBC_Settings_For_User\",\"2\",\"dc.conf.template.Configures_the_ODBC_datasource_name_to_enable_user_to_connect_to_a_database_using_ODBC\"],[\"9505000000008510\",\"9505000000008498\",\"dc.conf.template.Create_Alternate_Local_Admin_Account\",\"1\",\"dc.conf.template.Creates_an_alternative_local_administrative_user_account\"],[\"9505000000008564\",\"9505000000008528\",\"dc.conf.template.Defrag_Hard_Disk\",\"1\",\"dc.conf.template.Defragments_the_fragmented_hard_disk_and_improves_the_performance\"],[\"9505000000008603\",\"9505000000008594\",\"dc.conf.template.Delete_Local_Admin_Account\",\"1\",\"dc.conf.template.Deletes_the_local_Administrator_account_to_enhance_security\"],[\"9505000000009547\",\"9505000000009540\",\"dc.conf.template.Disable_access_to_the_Windows_update_feature\",\"2\",\"dc.conf.template.Disables_user_from_accessing_the_Windows_update_feature\"],[\"9505000000009945\",\"9505000000009938\",\"dc.conf.template.Disable_Acrobat_X_Updater\",\"1\",\"dc.conf.template.Disables_the_automatic_updates_of_Acrobat_X\"],[\"9505000000009967\",\"9505000000009960\",\"dc.conf.template.Disable_Acrobat_XI_Updater\",\"1\",\"dc.conf.template.Disables_the_automatic_updates_of_Acrobat_XI\"],[\"9505000000009375\",\"9505000000009366\",\"dc.conf.template.Disable_Adobe_Acrobat_Reader_DC_updater\",\"1\",\"dc.conf.template.Disables_the_automatic_updates_of_Acrobat_Reader_DC\"],[\"9505000000009349\",\"9505000000009340\",\"dc.conf.template.Disable_Adobe_AIR_Updater\",\"1\",\"dc.conf.template.Disables_the_automatic_updates_of_Adobe_AIR_that_is_installed_at_the_system_level\"],[\"9505000000009207\",\"9505000000009198\",\"dc.conf.template.Disable_Adobe_Reader_10_updater\",\"1\",\"dc.conf.template.Disables_the_automatic_updates_of_Adobe_Reader_10\"],[\"9505000000009233\",\"9505000000009224\",\"dc.conf.template.Disable_Adobe_Reader_11_updater\",\"1\",\"dc.conf.template.Disables_the_automatic_updates_of_Adobe_Reader_11\"],[\"9505000000009111\",\"9505000000009102\",\"dc.conf.template.Disable_Adobe_Reader_8_updater\",\"1\",\"dc.conf.template.Disables_the_automatic_updates_of_Adobe_Reader_8\"],[\"9505000000009181\",\"9505000000009172\",\"dc.conf.template.Disable_Adobe_Reader_9_updater\",\"1\",\"dc.conf.template.Disables_the_automatic_updates_of_Adobe_Reader_9\"],[\"9505000000009259\",\"9505000000009250\",\"dc.conf.template.Disable_Adobe_Shockwave_11_updater\",\"1\",\"dc.conf.template.Disables_the_automatic_updates_of_Adobe_Shockwave_11\"],[\"9505000000008177\",\"9505000000008164\",\"dc.conf.template.Disable_Auto_Update_of_Google_Chrome_title\",\"1\",\"dc.conf.template.Disable_Auto_Update_of_Google_Chrome_desc\"],[\"9505000000009897\",\"9505000000009890\",\"dc.conf.template.Disable_automatic_app_updates_title\",\"1\",\"dc.conf.template.Disable_automatic_app_updates_desc\"],[\"9505000000009041\",\"9505000000009034\",\"dc.conf.template.Disable_Automatic_Delivery_of_IE_10\",\"1\",\"dc.conf.template.Disables_Internet_Explorer_from_upgrading_to_version_10_through_Windows_updates\"]],\"viewColumns\":[\"template_name\",\"Type\",\"template_desc\",\"collectionID\"],\"columnNames\":[\"templateID\",\"collectionID\",\"template_name\",\"Type\",\"template_desc\"],\"rowSelectionType\":\"NONE\",\"uniqueId\":\"ConfigTemplateView\"}}");
        String decodedJson = decodeGzip(gzippedData);
        System.out.println("GZipped JSON: " + gzippedData);
        System.out.println("Decoded JSON: " + decodedJson);
    }

    public static byte[] gzipJson(String jsonString) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
            gzipOutputStream.write(jsonString.getBytes());
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static String decodeGzip(byte[] gzippedData) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(gzippedData);
        GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = gzipInputStream.read(buffer)) > 0) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        return byteArrayOutputStream.toString("UTF-8");
    }
}
