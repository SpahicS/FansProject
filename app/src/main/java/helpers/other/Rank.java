package helpers.other;

import digitalbath.fansproject.R;

/**
 * Created by unexpected_err on 15/08/2017.
 */

public enum Rank {

    BRONZE("Bronze", R.color.bronze),
    SILVER("Silver", R.color.silver),
    GOLD("Gold", R.color.gold),
    PLATINUM("Platinum", R.color.platinum);

    private String mRankLabel;
    private int mRankBadgeColor;

    Rank(String titleResId, int imageResId) {
        mRankLabel = titleResId;
        mRankBadgeColor = imageResId;
    }

    public String getRankLabel() {
        return mRankLabel;
    }

    public int getRankBadgeColor() {
        return mRankBadgeColor;
    }

    public static Rank getRank(int numberOfPosts) {

        if (numberOfPosts < 10) {

                return BRONZE;

        } else if (numberOfPosts >= 10 && numberOfPosts < 20) {

            return SILVER;

        } else if (numberOfPosts >= 20 && numberOfPosts < 50) {

            return GOLD;

        } else if (numberOfPosts >= 50) {

            return PLATINUM;

        }

        return BRONZE;
    }
}