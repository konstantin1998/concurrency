package ru.mipt;
/*
public class  QueueConsensus<T> extends ConsensusProtocol<T> {
    private Queue queue;

    public QueueConsensus() {
         queue = new Queue();
    }
    protected void propose(T value) {
        queue.enq(value);
    }

    public T decide(T value) {
        propose(value);
        return queue.peek();
    }
}*/

/*
Все потоки кладут свои значения в очередь, таким образом, значение, которое положил первый поток будет в начале.
Поскольку метод peek() возврашает значение первого элемента без удаления, то все потоки будут получать значение,
которое предложил первый поток.
* */
