package delivery.santa.delivery.customer;

/**
 * Created by delivering on 12/9/2015.
 */
public class ExampleModel // display a text in the RecyclerView
{
    private final String mText;

    public ExampleModel(String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }
}