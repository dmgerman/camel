begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.resequencer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|resequencer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Timer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorServiceHelper
import|;
end_import

begin_comment
comment|/**  * Resequences elements based on a given {@link SequenceElementComparator}.  * This resequencer is designed for resequencing element streams. Stream-based  * resequencing has the advantage that the number of elements to be resequenced  * need not be known in advance. Resequenced elements are delivered via a  * {@link SequenceSender}.  *<p>  * The resequencer's behaviour for a given comparator is controlled by the  *<code>timeout</code> property. This is the timeout (in milliseconds) for a  * given element managed by this resequencer. An out-of-sequence element can  * only be marked as<i>ready-for-delivery</i> if it either times out or if it  * has an immediate predecessor (in that case it is in-sequence). If an  * immediate predecessor of a waiting element arrives the timeout task for the  * waiting element will be cancelled (which marks it as<i>ready-for-delivery</i>).  *<p>  * If the maximum out-of-sequence time difference between elements within a  * stream is known, the<code>timeout</code> value should be set to this  * value. In this case it is guaranteed that all elements of a stream will be  * delivered in sequence via the {@link SequenceSender}. The lower the  *<code>timeout</code> value is compared to the out-of-sequence time  * difference between elements within a stream the higher the probability is for  * out-of-sequence elements delivered by this resequencer. Delivery of elements  * must be explicitly triggered by applications using the {@link #deliver()} or  * {@link #deliverNext()} methods. Only elements that are<i>ready-for-delivery</i>  * are delivered by these methods. The longer an application waits to trigger a  * delivery the more elements may become<i>ready-for-delivery</i>.  *<p>  * The resequencer remembers the last-delivered element. If an element arrives  * which is the immediate successor of the last-delivered element it is  *<i>ready-for-delivery</i> immediately. After delivery the last-delivered  * element is adjusted accordingly. If the last-delivered element is  *<code>null</code> i.e. the resequencer was newly created the first arriving  * element needs<code>timeout</code> milliseconds in any case for becoming  *<i>ready-for-delivery</i>.  *<p>  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ResequencerEngine
specifier|public
class|class
name|ResequencerEngine
parameter_list|<
name|E
parameter_list|>
block|{
comment|/**      * The element that most recently hash been delivered or<code>null</code>      * if no element has been delivered yet.      */
DECL|field|lastDelivered
specifier|private
name|Element
argument_list|<
name|E
argument_list|>
name|lastDelivered
decl_stmt|;
comment|/**      * Minimum amount of time to wait for out-of-sequence elements.      */
DECL|field|timeout
specifier|private
name|long
name|timeout
decl_stmt|;
comment|/**      * A sequence of elements for sorting purposes.      */
DECL|field|sequence
specifier|private
name|Sequence
argument_list|<
name|Element
argument_list|<
name|E
argument_list|>
argument_list|>
name|sequence
decl_stmt|;
comment|/**      * A timer for scheduling timeout notifications.      */
DECL|field|timer
specifier|private
name|Timer
name|timer
decl_stmt|;
comment|/**      * A strategy for sending sequence elements.      */
DECL|field|sequenceSender
specifier|private
name|SequenceSender
argument_list|<
name|E
argument_list|>
name|sequenceSender
decl_stmt|;
comment|/**      * Creates a new resequencer instance with a default timeout of 2000      * milliseconds.      *      * @param comparator a sequence element comparator.      */
DECL|method|ResequencerEngine (SequenceElementComparator<E> comparator)
specifier|public
name|ResequencerEngine
parameter_list|(
name|SequenceElementComparator
argument_list|<
name|E
argument_list|>
name|comparator
parameter_list|)
block|{
name|this
operator|.
name|sequence
operator|=
name|createSequence
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
name|this
operator|.
name|timeout
operator|=
literal|2000L
expr_stmt|;
name|this
operator|.
name|lastDelivered
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
name|timer
operator|=
operator|new
name|Timer
argument_list|(
name|ExecutorServiceHelper
operator|.
name|getThreadName
argument_list|(
literal|"Stream Resequencer Timer"
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Stops this resequencer (i.e. this resequencer's {@link Timer} instance).      */
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|timer
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns the number of elements currently maintained by this resequencer.      *      * @return the number of elements currently maintained by this resequencer.      */
DECL|method|size ()
specifier|public
specifier|synchronized
name|int
name|size
parameter_list|()
block|{
return|return
name|sequence
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * Returns this resequencer's timeout value.      *      * @return the timeout in milliseconds.      */
DECL|method|getTimeout ()
specifier|public
name|long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
comment|/**      * Sets this sequencer's timeout value.      *      * @param timeout the timeout in milliseconds.      */
DECL|method|setTimeout (long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
comment|/**      * Returns the sequence sender.      *      * @return the sequence sender.      */
DECL|method|getSequenceSender ()
specifier|public
name|SequenceSender
argument_list|<
name|E
argument_list|>
name|getSequenceSender
parameter_list|()
block|{
return|return
name|sequenceSender
return|;
block|}
comment|/**      * Sets the sequence sender.      *      * @param sequenceSender a sequence element sender.      */
DECL|method|setSequenceSender (SequenceSender<E> sequenceSender)
specifier|public
name|void
name|setSequenceSender
parameter_list|(
name|SequenceSender
argument_list|<
name|E
argument_list|>
name|sequenceSender
parameter_list|)
block|{
name|this
operator|.
name|sequenceSender
operator|=
name|sequenceSender
expr_stmt|;
block|}
comment|/**      * Returns the last delivered element.      *      * @return the last delivered element or<code>null</code> if no delivery      *         has been made yet.      */
DECL|method|getLastDelivered ()
name|E
name|getLastDelivered
parameter_list|()
block|{
if|if
condition|(
name|lastDelivered
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|lastDelivered
operator|.
name|getObject
argument_list|()
return|;
block|}
comment|/**      * Sets the last delivered element. This is for testing purposes only.      *      * @param o an element.      */
DECL|method|setLastDelivered (E o)
name|void
name|setLastDelivered
parameter_list|(
name|E
name|o
parameter_list|)
block|{
name|lastDelivered
operator|=
operator|new
name|Element
argument_list|<
name|E
argument_list|>
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
comment|/**      * Inserts the given element into this resequencer. If the element is not      * ready for immediate delivery and has no immediate presecessor then it is      * scheduled for timing out. After being timed out it is ready for delivery.      *      * @param o an element.      */
DECL|method|insert (E o)
specifier|public
specifier|synchronized
name|void
name|insert
parameter_list|(
name|E
name|o
parameter_list|)
block|{
comment|// wrap object into internal element
name|Element
argument_list|<
name|E
argument_list|>
name|element
init|=
operator|new
name|Element
argument_list|<
name|E
argument_list|>
argument_list|(
name|o
argument_list|)
decl_stmt|;
comment|// add element to sequence in proper order
name|sequence
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|Element
argument_list|<
name|E
argument_list|>
name|successor
init|=
name|sequence
operator|.
name|successor
argument_list|(
name|element
argument_list|)
decl_stmt|;
comment|// check if there is an immediate successor and cancel
comment|// timer task (no need to wait any more for timeout)
if|if
condition|(
name|successor
operator|!=
literal|null
condition|)
block|{
name|successor
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
comment|// start delivery if current element is successor of last delivered element
if|if
condition|(
name|successorOfLastDelivered
argument_list|(
name|element
argument_list|)
condition|)
block|{
comment|// nothing to schedule
block|}
elseif|else
if|if
condition|(
name|sequence
operator|.
name|predecessor
argument_list|(
name|element
argument_list|)
operator|!=
literal|null
condition|)
block|{
comment|// nothing to schedule
block|}
else|else
block|{
name|element
operator|.
name|schedule
argument_list|(
name|defineTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Delivers all elements which are currently ready to deliver.      *      * @throws Exception thrown by {@link SequenceSender#sendElement(Object)}.      *      * @see ResequencerEngine#deliverNext()       */
DECL|method|deliver ()
specifier|public
specifier|synchronized
name|void
name|deliver
parameter_list|()
throws|throws
name|Exception
block|{
while|while
condition|(
name|deliverNext
argument_list|()
condition|)
block|{
comment|// do nothing here
block|}
block|}
comment|/**      * Attempts to deliver a single element from the head of the resequencer      * queue (sequence). Only elements which have not been scheduled for timing      * out or which already timed out can be delivered. Elements are deliveref via      * {@link SequenceSender#sendElement(Object)}.      *      * @return<code>true</code> if the element has been delivered      *<code>false</code> otherwise.      *      * @throws Exception thrown by {@link SequenceSender#sendElement(Object)}.      *      */
DECL|method|deliverNext ()
specifier|public
name|boolean
name|deliverNext
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|sequence
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// inspect element with lowest sequence value
name|Element
argument_list|<
name|E
argument_list|>
name|element
init|=
name|sequence
operator|.
name|first
argument_list|()
decl_stmt|;
comment|// if element is scheduled do not deliver and return
if|if
condition|(
name|element
operator|.
name|scheduled
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// remove deliverable element from sequence
name|sequence
operator|.
name|remove
argument_list|(
name|element
argument_list|)
expr_stmt|;
comment|// set the delivered element to last delivered element
name|lastDelivered
operator|=
name|element
expr_stmt|;
comment|// deliver the sequence element
name|sequenceSender
operator|.
name|sendElement
argument_list|(
name|element
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
comment|// element has been delivered
return|return
literal|true
return|;
block|}
comment|/**      * Returns<code>true</code> if the given element is the immediate      * successor of the last delivered element.      *      * @param element an element.      * @return<code>true</code> if the given element is the immediate      *         successor of the last delivered element.      */
DECL|method|successorOfLastDelivered (Element<E> element)
specifier|private
name|boolean
name|successorOfLastDelivered
parameter_list|(
name|Element
argument_list|<
name|E
argument_list|>
name|element
parameter_list|)
block|{
if|if
condition|(
name|lastDelivered
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|sequence
operator|.
name|comparator
argument_list|()
operator|.
name|successor
argument_list|(
name|element
argument_list|,
name|lastDelivered
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Creates a timeout task based on the timeout setting of this resequencer.      *      * @return a new timeout task.      */
DECL|method|defineTimeout ()
specifier|private
name|Timeout
name|defineTimeout
parameter_list|()
block|{
return|return
operator|new
name|Timeout
argument_list|(
name|timer
argument_list|,
name|timeout
argument_list|)
return|;
block|}
DECL|method|createSequence (SequenceElementComparator<E> comparator)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Sequence
argument_list|<
name|Element
argument_list|<
name|E
argument_list|>
argument_list|>
name|createSequence
parameter_list|(
name|SequenceElementComparator
argument_list|<
name|E
argument_list|>
name|comparator
parameter_list|)
block|{
return|return
operator|new
name|Sequence
argument_list|<
name|Element
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|(
operator|new
name|ElementComparator
argument_list|<
name|E
argument_list|>
argument_list|(
name|comparator
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

