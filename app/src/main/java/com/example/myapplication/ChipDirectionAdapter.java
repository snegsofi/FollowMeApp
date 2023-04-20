package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.List;

public class ChipDirectionAdapter extends RecyclerView.Adapter<ChipDirectionAdapter.ViewHolder> {

    List<ChipDirectionModel> ChipDirectionModelList;
    Context context;

    // конструктор
    public ChipDirectionAdapter(Context context, List<ChipDirectionModel> ChipDirectionModelList, ChipDirectionAdapter.ItemClickListener clickListener) {
        this.context=context;
        this.ChipDirectionModelList = ChipDirectionModelList;
        this.clickListener=clickListener;
    }

    // создание новой ячейки
    @NonNull
    @Override
    public ChipDirectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // класс, создающий из содержимого layout-файла View-элемент
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.chip_direction_rv, parent, false);

        return new ChipDirectionAdapter.ViewHolder(contactView);
    }

    // заполнение ячейки данными
    @Override
    public void onBindViewHolder(@NonNull ChipDirectionAdapter.ViewHolder holder, int position) {

        holder.guestChip.setText(ChipDirectionModelList.get(position).getTitle());
        holder.guestChip.setChecked(ChipDirectionModelList.get(position).isChecked());

        // обработка нажатия на элемент
        holder.guestChip.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                clickListener.onChipDirectionItemClick(holder.getAdapterPosition(),
                        holder.guestChip.getText().toString());
            }
        });

    }

    // возвращает количество элементов в списке
    @Override
    public int getItemCount() {
        return ChipDirectionModelList.size();
    }

    // инициализация компонентов
    public class ViewHolder extends RecyclerView.ViewHolder {
        // описывает и предоставляет доступ ко всем представлениям в каждой строке элемента
        public Chip guestChip;

        public ViewHolder(View itemView) {
            super(itemView);

            guestChip=itemView.findViewById(R.id.chip);
        }
    }


    private ItemClickListener clickListener;

    public interface ItemClickListener{
        void onChipDirectionItemClick(int position, String text);
    }
}
