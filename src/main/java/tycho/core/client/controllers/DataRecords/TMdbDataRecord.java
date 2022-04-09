package tycho.core.client.controllers.DataRecords;

import java.io.File;
import java.util.List;

public record TMdbDataRecord(List<Integer> seasons, int tmdbId, File posterFile, String title, int year){}
