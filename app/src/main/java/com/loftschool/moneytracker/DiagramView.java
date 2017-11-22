package com.loftschool.moneytracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

public class DiagramView extends View{

    public long expense;
    public long income;

    final Paint expensesPaint = new Paint();
    final Paint incomesPaint = new Paint();

    public DiagramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        expensesPaint.setColor(getResources().getColor(R.color.spending_color));
        incomesPaint.setColor(getResources().getColor(R.color.incomes_color));
    }

    public void update(long expense, long income) {
        this.expense = expense;
        this.income = income;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (expense + income == 0) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawPieDiagram(canvas);
        } else {
            drawRectDiagram(canvas);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawPieDiagram(Canvas canvas) {
        float expensesAnlge = 360.f * expense/(expense + income);
        float incomesAnlge = 360.f * income/(expense + income);
        int space = 10;
        int size = Math.min(getWidth(), getHeight()) - 2 * space;
        final int xMargin = (getWidth() - size) / 2;
        final int yMargin = (getHeight() - size) / 2;

        canvas.drawArc(xMargin - space, yMargin, getWidth() - xMargin - space,
                getHeight() - yMargin, 180 - expensesAnlge / 2, expensesAnlge, true, expensesPaint);
        canvas.drawArc(xMargin + space, yMargin, getWidth() - xMargin + space,
                getHeight() - yMargin, 360 - incomesAnlge / 2, incomesAnlge, true, incomesPaint);
    }
    private void drawRectDiagram(Canvas canvas) {
        int space = 10;
        int size = Math.min(getWidth(), getHeight()) - 2 * space;
        int xMargin = (getWidth() - size) / 2;
        int yMargin = (getHeight() - size) / 2;

        canvas.drawRect(xMargin - space, yMargin, getWidth() - xMargin - space,
                getHeight() - yMargin, expensesPaint);
        canvas.drawRect(xMargin + space, yMargin, getWidth() - xMargin + space,
                getHeight() - yMargin, incomesPaint);
    }
}
