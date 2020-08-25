import WordCountActor.TextMessage
import akka.actor.typed.{ActorSystem, Behavior, PostStop, Signal}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}

object WordCountActor {
  case class TextMessage(content: String)
  def apply(): Behavior[TextMessage] =
    Behaviors.setup[TextMessage](context => new WordCountActor(context))
}

class WordCountActor(context: ActorContext[TextMessage]) extends AbstractBehavior[TextMessage](context) {
  context.log.info("IoT Application started")

  override def onMessage(msg: TextMessage): Behavior[TextMessage] = {
    msg match
    {
      case TextMessage(content) => {
        println(s"Word count: ${content.split(" ").length}")
      }
    }
    // No need to handle any messages
    Behaviors.same
  }

  override def onSignal: PartialFunction[Signal, Behavior[TextMessage]] = {
    case PostStop =>
      context.log.info("IoT Application stopped")
      this
  }
}

object WordCountApp extends App {
  val wordCountActor = ActorSystem(WordCountActor(),"wordCountActor")
  wordCountActor ! TextMessage("My name is hieu")
}

//object WordCount extends App {
//
//  case class TextMessage(content: String)
//
//  val wordCountActor = ActorSystem(
//    Behaviors.receiveMessage[TextMessage] { message: TextMessage =>
//      message match {
//        case TextMessage(content) => {
//          println(s"Total words: ${content.split(" ").length}")
//        }
//      }
//      Behaviors.same
//    },"wordCount")
//
//  wordCountActor ! TextMessage("Hello Minh Hieu")
//}
