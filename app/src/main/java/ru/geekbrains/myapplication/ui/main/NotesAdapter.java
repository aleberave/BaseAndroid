package ru.geekbrains.myapplication.ui.main;

import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import ru.geekbrains.myapplication.R;
import ru.geekbrains.myapplication.repository.NoteData;
import ru.geekbrains.myapplication.repository.NotesSource;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private NotesSource notesSource;
    private OnItemClickListener onItemClickListener;

    private final Fragment fragment;

    private int menuPosition;

    public int getMenuPosition() {
        return menuPosition;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    NotesAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setData(NotesSource notesSource) {
        this.notesSource = notesSource;
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
        return new MyViewHolder(layoutInflater.inflate(R.layout.fragment_notes_recycler_card_view_item, parent, false));
    }

    //связывает ViewHolder с контентом по позиции в списке,
    // вызывается хоть 100 раз, если все элементы просматривать
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindContentWithLayout(notesSource.getNoteData(position));
    }

    //размер элементов списка
    @Override
    public int getItemCount() {
        return notesSource.size();
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

            fragment.registerForContextMenu(itemView);

            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
//                    точные координаты для контекстного меню
//                    view.showContextMenu(100, 100);
//                    return true;
                    menuPosition = getLayoutPosition();
                    return false; // обработка клика и передать дальше для действия во фрагменте
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    menuPosition = getLayoutPosition();
                    // getAdapterPosition() -  позиция элемента в фоне(например пришли данные с сервера)
                    // getLayoutPosition() - позиция элемента на экране у пользователя
                    return false; // обработка клика и передать дальше для действия во фрагменте
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    menuPosition = getLayoutPosition();
                    onItemClickListener.onItemClick(menuPosition);
                }
            });
        }

        // связывает контент с макетом
        public void bindContentWithLayout(NoteData content) {
            textViewTitle.setText(content.getTitle());
            textViewDescriptionDate.setText(content.getDate().toString());
            textViewDescription.setText(content.getDescription());
            imageView.setImageResource(content.getPicture());
            imageView.setBackgroundTintMode(PorterDuff.Mode.ADD);
            imageView.setBackgroundColor(fragment.getResources().getColor(content.getPictureColor(),
                    fragment.requireActivity().getTheme()));
            like.setChecked(content.isLike());
        }

    }

}

