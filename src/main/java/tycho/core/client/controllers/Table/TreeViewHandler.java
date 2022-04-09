package tycho.core.client.controllers.Table;

import tycho.core.client.CustomFingerprintDB.DB;
import tycho.core.client.CustomFingerprintDB.DBFolder;
import tycho.core.client.fingerprint.Fingerprint;
import tycho.core.misc.AlertUtils;
import tycho.core.tmdb.TMdb;
import tycho.core.tmdb.model.TvSeason;
import tycho.core.tmdb.model.TvShow;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.List;

public class TreeViewHandler {

    private final TreeItem<String> root;
    private final TvShow tvShow;
    private final DBFolder folder;

    public TreeViewHandler(TreeView<String> treeView, int tmdbId){
        folder = DB.getInstance().byTMdbId(tmdbId);
        tvShow = TMdb.getInstance().getTvShow(tmdbId);

        root = new TreeItem<>("Seasons");
        root.setExpanded(true);

        if(folder == null){
            Platform.runLater(() -> treeView.setRoot(null));
            AlertUtils.showMsg("No items found", "Browser");
            return;
        }
        addAllSeasons();

        Platform.runLater(() -> treeView.setRoot(root));
    }

    private void addAllSeasons(){
        tvShow.getSeasons().forEach(this::addSeason);
    }

    private void addSeason(TvSeason season){
        List<Fingerprint> episodes = folder.getAllFingerprintsForSeason(season.getSeasonNumber());
        TreeItem<String> seasonItem = new TreeItem<>(String.format("Season %d (%d/%d)"
                                                        , season.getSeasonNumber()
                                                        , episodes.size()
                                                        , TMdb.getInstance().getTvSeason(folder.getTMdbId(), season.getSeasonNumber()).getEpisodes().size()));
        seasonItem.setExpanded(true);

        for(Fingerprint ep : episodes){
            TreeItem<String> epItem = new TreeItem<>(String.format("Episode %d", ep.getEpisode()));

            seasonItem.getChildren().add(epItem);
        }


        root.getChildren().add(seasonItem);
    }



}
