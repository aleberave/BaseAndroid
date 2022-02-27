package ru.geekbrains.myapplication.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.geekbrains.myapplication.R;
import ru.geekbrains.myapplication.repository.CardData;
import ru.geekbrains.myapplication.repository.CardSource;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private CardSource cardSource;
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setData(CardSource cardSource) {
        this.cardSource = cardSource;
        // команда адаптеру отрисовать все(!) полученные данные
        // связано с излишними зататами ресурсов
        notifyDataSetChanged();
    }

    //создаёт и вызывает для каждого элемента списка
    // (если список из 5 элементов,
    // то создаваться и вызываться будет 9 раз (2до, 5видимых, 2после)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(layoutInflater.inflate(R.layout.fragment_notes_cardview_item, parent, false));
    }

    //связывает ViewHolder с контентом по опзиции в списке,
    // вызывается хоть 100 раз, если все элементы просматривать
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindContentWithLayout(cardSource.getCardDAta(position));
    }

    //размер элементов списка
    @Override
    public int getItemCount() {
        return cardSource.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewTitle;
        private final TextView textViewDescriptionDate;
        private final TextView textViewDescription;
        private final ImageView imageView;
        private final ToggleButton like;

        //itemView - это макет элемента списка в нашем случае textView
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.title);
            textViewDescriptionDate = (TextView) itemView.findViewById(R.id.description_date);
            textViewDescription = (TextView) itemView.findViewById(R.id.description);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            like = (ToggleButton) itemView.findViewById(R.id.like);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getLayoutPosition());
                        // getAdapterPosition() -  позиция элемента в фоне(например пришли данные с сервера)
                        // getLayoutPosition() - позиция элемента на экране у пользователя
                    }
                }
            });
        }

        // связывает контент с макетом
        public void bindContentWithLayout(CardData content) {
            textViewTitle.setText(content.getTitle());
            textViewDescriptionDate.setText(content.getDate().toString());
            textViewDescription.setText(content.getDescription());
            imageView.setImageResource(content.getPicture());
            like.setChecked(content.isLike());
        }

    }

}

