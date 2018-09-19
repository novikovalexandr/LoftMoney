package com.alexandr4.loftmoney;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.alexandr4.loftmoney.Item.TYPE_EXPENSE;
import static com.alexandr4.loftmoney.Item.TYPE_INCOME;

/**
 * A simple {@link Fragment} subclass.
 */
public class BalanceFragment extends Fragment {

    public static BalanceFragment newInstance() {
        Bundle args = new Bundle();
        BalanceFragment fragment = new BalanceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public BalanceFragment() {

    }

    private Api api;
    private TextView income;
    private TextView balance;
    private TextView expense;
    private DiagramView diagram;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = ((App) getActivity().getApplication()).getApi();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_balance, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        income = (TextView) view.findViewById(R.id.income_value);
        balance = (TextView) view.findViewById(R.id.balance_value);
        expense = (TextView) view.findViewById(R.id.expense_value);
        diagram = (DiagramView) view.findViewById(R.id.diagram_value);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed())
            updateData();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    private void updateData() {
        Call<BalanceResult> call = api.balance();
        call.enqueue(new Callback<BalanceResult>() {
            @Override
            public void onResponse(@NonNull Call<BalanceResult> call, @NonNull Response<BalanceResult> response) {
                BalanceResult result = response.body();
                income.setText(String.valueOf(result.getTotalIncome()));
                expense.setText(String.valueOf(result.getTotalExpenses()));
                balance.setText(String.valueOf(result.getTotalIncome() - result.getTotalExpenses()));
                diagram.update(result.getTotalIncome(), result.getTotalExpenses());
            }

            @Override
            public void onFailure(@NonNull Call<BalanceResult> call, @NonNull Throwable t) {
                showError(getString(R.string.loadItem_error_message));
            }
        });

    }

    private void showError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }
}

