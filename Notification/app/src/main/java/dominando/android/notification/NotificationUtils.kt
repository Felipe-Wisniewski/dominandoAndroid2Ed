package dominando.android.notification

import android.app.Notification
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat

class NotificationUtils {

    companion object {

        val ACAO_DELETE = "dominando.android.notification.DELETE_NOTIFICACAO"
        val ACAO_NOTIFICACAO = "dominando.android.notification.ACAO_NOTIFICACAO"

        private fun criarPendingIntent(ctx: Context, texto: String, id: Int): PendingIntent{

            val resultIntent = Intent(ctx, DetalheActivity::class.java)
            resultIntent.putExtra(DetalheActivity.EXTRA_TEXTO, texto)

            val stackBuilder = TaskStackBuilder.create(ctx)
            stackBuilder.addParentStack(DetalheActivity::class.java)
            stackBuilder.addNextIntent(resultIntent)

            return stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        fun criarNotificacaoSimples(ctx: Context, texto: String, id: Int){

            val resultPendingIntent = criarPendingIntent(ctx, texto, id)

            val mBuilder = NotificationCompat.Builder(ctx)
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setSmallIcon(R.drawable.ic_notificacao)
                                .setContentTitle("Simples $id")
                                .setContentText(texto)
                                .setContentIntent(resultPendingIntent)

            val nm = NotificationManagerCompat.from(ctx)
            nm.notify(id, mBuilder.build())
        }

        fun criarNotificacaoCompleta(ctx: Context, texto: String, id: Int){

            val uriSom = Uri.parse("android.resource://" + ctx.packageName + "/raw/som_notificacao")
            val pitAcao = PendingIntent.getBroadcast(ctx, 0, Intent(ACAO_NOTIFICACAO), 0)
            val pitDelete = PendingIntent.getBroadcast(ctx, 0, Intent(ACAO_DELETE), 0)
            val largeIcon = BitmapFactory.decodeResource(ctx.resources, R.mipmap.ic_launcher)
            val pitNotificacao = criarPendingIntent(ctx, texto, id)

            val mBuilder = NotificationCompat.Builder(ctx)
                    .setSmallIcon(R.drawable.ic_notificacao)
                    .setColor(Color.RED)
                    .setContentTitle("Completa")
                    .setContentText(texto)
                    .setTicker("Chegou uma notificação")
                    .setWhen(System.currentTimeMillis())
                    .setLargeIcon(largeIcon)
                    .setAutoCancel(true)
                    .setContentIntent(pitNotificacao)
                    .setDeleteIntent(pitDelete)
                    .setLights(Color.BLUE, 1000, 5000)
                    .setSound(uriSom)
                    .setVibrate(longArrayOf(100, 500, 200, 800))
                    .addAction(R.drawable.ic_acao_notificacao, "Ação Customizada", pitAcao)
                    .setNumber(id)
                    .setSubText("Subtexto")

            val nm = NotificationManagerCompat.from(ctx)
            nm.notify(id, mBuilder.build())
        }

        fun criarNotificationBig(ctx: Context, idNotificacao: Int){

            val numero = 5

            val inboxStyle = NotificationCompat.InboxStyle()
            inboxStyle.setBigContentTitle("Mensagens não lidas: ")
            for (i in 1..numero) inboxStyle.addLine("Mensagem $i")
            inboxStyle.setSummaryText("Clique para exibir")

            val mBuilder = NotificationCompat.Builder(ctx)
                    .setSmallIcon(R.drawable.ic_notificacao)
                    .setColor(Color.RED)
                    .setContentTitle("Notificação")
                    .setContentText("Vários itens pendentes")
                    .setContentIntent(criarPendingIntent(ctx, "Mensagens não lidas", -1))
                    .setNumber(numero)
                    .setStyle(inboxStyle)

            val nm = NotificationManagerCompat.from(ctx)
            nm.notify(idNotificacao, mBuilder.build())
        }
    }
}