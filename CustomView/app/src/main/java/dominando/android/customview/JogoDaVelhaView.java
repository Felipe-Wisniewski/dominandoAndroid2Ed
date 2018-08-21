package dominando.android.customview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class JogoDaVelhaView extends View {

    public static final int XIS = 1;
    public static final int BOLA = 2;
    public static final int VAZIO = 0;
    public static final int EMPATE = 3;

    JogoDaVelhaListener mListener;
    GestureDetector mDetector;

    int mCorDaBarra;
    float mLarguraBarra;

    int mTamanho;
    int mVez;
    int[][] mTabuleiro;

    Paint mPaint;
    Bitmap mImageX;
    Bitmap mImageO;

    public JogoDaVelhaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mVez = XIS;
        mTabuleiro = new int[3][3];

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.JogoDaVelha);
        mCorDaBarra = a.getColor(R.styleable.JogoDaVelha_corDaBarra, Color.BLACK);
        mLarguraBarra = a.getDimension(R.styleable.JogoDaVelha_larguraDaBarra, 3);
    }

    public interface JogoDaVelhaListener{
        void fimDeJogo(int vencedor);
    }

    public void setListener(JogoDaVelhaListener listener){
        this.mListener = listener;
    }

    @Override
    protected void onAttachedToWindow(){
        super.onAttachedToWindow();

        mDetector = new GestureDetector(this.getContext(), new VelhaTouchListener());

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);

        mImageX = BitmapFactory.decodeResource(getResources(), R.drawable.x_mark);
        mImageO = BitmapFactory.decodeResource(getResources(), R.drawable.o_mark);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        mDetector = null;
        mTabuleiro = null;
        mPaint = null;
        mImageX = null;
        mImageO = null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT){
            Resources r = getResources();
            float quadrante = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 48, r.getDisplayMetrics());
            mTamanho = (int) quadrante * 3;
        }else if(getLayoutParams().width == ViewGroup.LayoutParams.MATCH_PARENT){
            mTamanho = Math.min(
                    MeasureSpec.getSize(widthMeasureSpec),
                    MeasureSpec.getSize(heightMeasureSpec));
        }else{
            mTamanho = getLayoutParams().width;
        }
        setMeasuredDimension(mTamanho, mTamanho);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int quadrante = mTamanho / 3;

        //Desenhando as linhas
        mPaint.setColor(mCorDaBarra);
        mPaint.setStrokeWidth(mLarguraBarra);

        //VERTICAIS:
        canvas.drawLine(quadrante, 0, quadrante, mTamanho, mPaint);
        canvas.drawLine(quadrante * 2, 0, quadrante * 2, mTamanho, mPaint);

        //HORIZONTAIS:
        canvas.drawLine(0, quadrante, mTamanho, quadrante, mPaint);
        canvas.drawLine(0, quadrante * 2, mTamanho, quadrante * 2, mPaint);

        for(int linha = 0; linha < 3; linha++){
            for(int coluna = 0; coluna < 3; coluna++){
                int x = coluna * quadrante;
                int y = linha * quadrante;
                Rect rect = new Rect(x, y, x + quadrante, y + quadrante);

                if(mTabuleiro[linha][coluna] == XIS){
                    canvas.drawBitmap(mImageX, null, rect, null);
                }else if(mTabuleiro[linha][coluna] == BOLA){
                    canvas.drawBitmap(mImageO, null, rect, null);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return mDetector.onTouchEvent(event);
    }

    public void reiniciarJogo() {
        mTabuleiro = new int[3][3];
        invalidate();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable p = super.onSaveInstanceState();
        EstadoJogo estado = new EstadoJogo(p, mVez, mTabuleiro);
        return estado;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state){
        EstadoJogo estado = (EstadoJogo) state;
        super.onRestoreInstanceState(estado.getSuperState());
        mVez = estado.vez;
        mTabuleiro = estado.tabuleiro;
        invalidate();
    }

    //----------------------------------CLASS-------------------------------------------------------
    class VelhaTouchListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            int vencedor = gameOver();

            if(e.getAction() == MotionEvent.ACTION_UP && vencedor == VAZIO){
                int quadrante = mTamanho / 3;

                int linha = (int) (e.getY() / quadrante);
                int coluna = (int) (e.getX() / quadrante);

                if(mTabuleiro[linha][coluna] == VAZIO){
                    mTabuleiro[linha][coluna] = mVez;
                    mVez = (mVez == XIS) ? BOLA : XIS;
                    invalidate();

                    vencedor = gameOver();
                    if(vencedor != VAZIO){
                        if(mListener != null){
                            mListener.fimDeJogo(vencedor);
                        }
                    }
                    return true;
                }
            }
            return super.onSingleTapUp(e);
        }

        private int gameOver() {
            //Horizontais
            if(ganhou(mTabuleiro[0][0], mTabuleiro[0][1], mTabuleiro[0][2])){return mTabuleiro[0][0];}

            if(ganhou(mTabuleiro[1][0], mTabuleiro[1][1], mTabuleiro[1][2])){return mTabuleiro[1][0];}

            if(ganhou(mTabuleiro[2][0], mTabuleiro[2][1], mTabuleiro[2][2])){return mTabuleiro[2][0];}

            //Verticais
            if(ganhou(mTabuleiro[0][0], mTabuleiro[1][0], mTabuleiro[2][0])){return mTabuleiro[0][0];}

            if(ganhou(mTabuleiro[0][1], mTabuleiro[1][1], mTabuleiro[2][1])){return mTabuleiro[0][1];}

            if(ganhou(mTabuleiro[0][2], mTabuleiro[1][2], mTabuleiro[2][2])){return mTabuleiro[0][2];}

            //Diagonais
            if(ganhou(mTabuleiro[0][0], mTabuleiro[1][1], mTabuleiro[2][2])){return mTabuleiro[0][0];}

            if(ganhou(mTabuleiro[0][2], mTabuleiro[1][1], mTabuleiro[2][0])){return mTabuleiro[0][2];}

            //Existem espaços vazios
            for(int linha = 0; linha < mTabuleiro.length; linha++){
                for(int coluna = 0; coluna < mTabuleiro[linha].length; coluna++){
                    if(mTabuleiro[linha][coluna] == VAZIO){
                        return VAZIO;
                    }
                }
            }
            return EMPATE;
        }

        private boolean ganhou(int a, int b, int c) {
            return (a == b && b == c && a != VAZIO);
        }
    }

    //--------------------------------CLASS---------------------------------------------------------
    static class EstadoJogo extends BaseSavedState {

        int vez;
        int[][] tabuleiro;

        private EstadoJogo(Parcelable p, int vez, int[][] tabuleiro) {
            super(p);

            this.vez = vez;
            this.tabuleiro = tabuleiro;
        }

        private EstadoJogo(Parcel p){
            super(p);

            vez = p.readInt();
            tabuleiro = new int[3][3];
            for (int linha = 0; linha < tabuleiro.length; linha++){
                p.readIntArray(tabuleiro[linha]);
            }
        }

        public final Parcelable.Creator<EstadoJogo> CREATOR = new Parcelable.Creator<EstadoJogo>(){

            @Override
            public EstadoJogo createFromParcel(Parcel source) {
                return new EstadoJogo(source);
            }

            @Override
            public EstadoJogo[] newArray(int size) {
                return new EstadoJogo[size];
            }
        };

        @Override
        public void writeToParcel(Parcel parcel, int flags){
            super.writeToParcel(parcel, flags);

            parcel.writeInt(vez);
            for (int linha = 0; linha < tabuleiro.length; linha++){
                parcel.writeIntArray(tabuleiro[linha]);
            }
        }
    }
}
