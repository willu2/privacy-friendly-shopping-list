package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deleteproducts.listadapter;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.R;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.business.domain.ProductDto;

/**
 * Description:
 * Author: Grebiel Jose Ifill Brito
 * Created: 21.07.16 creation date
 */
public class DeleteProductsItemViewHolder extends RecyclerView.ViewHolder
{
    private DeleteProductItemCache cache;
    private AppCompatActivity activity;

    public DeleteProductsItemViewHolder(final View parent, AppCompatActivity activity)
    {
        super(parent);
        this.cache = new DeleteProductItemCache(parent);
        this.activity = activity;
    }

    public void processDto(ProductDto dto)
    {
        cache.getProductNameTextView().setText(dto.getProductName());
        cache.getProductQuantityTextView().setText(dto.getQuantity());
        cache.getCheckBox().setChecked(dto.isChecked());
        updateVisibilityFormat(dto);

        cache.getProductCard().setAlpha(0.0f);
        cache.getProductCard().setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                dto.setSelectedForDeletion(!dto.isSelectedForDeletion());
                updateVisibilityFormat(dto);
            }
        });

    }

    private void updateVisibilityFormat(ProductDto dto)
    {
        Resources resources = cache.getProductCard().getContext().getResources();
        TextView productNameTextView = cache.getProductNameTextView();
        TextView productQuantityTextView = cache.getProductQuantityTextView();
        AppCompatCheckBox checkBox = (AppCompatCheckBox) cache.getCheckBox();
        if ( dto.isSelectedForDeletion() )
        {
            int transparent = resources.getColor(R.color.transparent);
            int grey = resources.getColor(R.color.middlegrey);

            cache.getProductCard().setCardBackgroundColor(transparent);
            productNameTextView.setTextColor(grey);
            productQuantityTextView.setTextColor(grey);
            checkBox.setSupportButtonTintList(ColorStateList.valueOf(grey));
            productNameTextView.setPaintFlags(productNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            productQuantityTextView.setPaintFlags(productQuantityTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else
        {
            int white = resources.getColor(R.color.white);
            int black = resources.getColor(R.color.black);

            cache.getProductCard().setCardBackgroundColor(white);
            productNameTextView.setTextColor(black);
            productQuantityTextView.setTextColor(black);
            checkBox.setSupportButtonTintList(ColorStateList.valueOf(black));
            productNameTextView.setPaintFlags(productNameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            productQuantityTextView.setPaintFlags(productQuantityTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }
}
