package ru.mipt;
/*
public class  QueueConsensus<T> extends ConsensusProtocol<T> {
    private static final int WIN = 0;
    private static final int LOSE = 1;
    private static int ultimateValueIndex = -1;
    Queue queue;

    public QueueConsensus() {
         queue = new Queue();
         queue.enq(WIN);
         queue.enq(LOSE);
    }

    public T decide(T value) {
        propose(value);
        int status = queue.peek();
        int i = ThreadID.get();
        if (status == WIN) {
            ultimateValueIndex = i;
            queue.deq();
        }
        return proposed[ultimateValueIndex];

    }
}*/

/*
Если взять за основу протокол консенсуса, описанный в учеюнике гл. 5, рисунок 5.7, с. 110 и переделать его так как
показано выше. Тогда первый поток, входя в метод decide сможет узнать, что он первый и в поле ultimateValueIndex
записать свой индекс как индекс, в котором лежит значение, которое должны получить все потоки. Все остальные потоки,
пришедшие в метод  decide после первого потока, будут доставать изочереди значение LOSE и получать значение, которое
предложил первый поток.
* */
