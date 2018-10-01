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

begin_comment
comment|/**  * A strategy for comparing {@link Element} instances. This strategy uses  * another {@link SequenceElementComparator} instance for comparing elements  * contained by {@link Element} instances.  */
end_comment

begin_class
DECL|class|ElementComparator
class|class
name|ElementComparator
parameter_list|<
name|E
parameter_list|>
implements|implements
name|SequenceElementComparator
argument_list|<
name|Element
argument_list|<
name|E
argument_list|>
argument_list|>
block|{
comment|/**      * A sequence element comparator this comparator delegates to.      */
DECL|field|comparator
specifier|private
specifier|final
name|SequenceElementComparator
argument_list|<
name|E
argument_list|>
name|comparator
decl_stmt|;
comment|/**      * Creates a new element comparator instance.      *       * @param comparator a sequence element comparator this comparator delegates to.      */
DECL|method|ElementComparator (SequenceElementComparator<E> comparator)
name|ElementComparator
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
name|comparator
operator|=
name|comparator
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|predecessor (Element<E> o1, Element<E> o2)
specifier|public
name|boolean
name|predecessor
parameter_list|(
name|Element
argument_list|<
name|E
argument_list|>
name|o1
parameter_list|,
name|Element
argument_list|<
name|E
argument_list|>
name|o2
parameter_list|)
block|{
return|return
name|comparator
operator|.
name|predecessor
argument_list|(
name|o1
operator|.
name|getObject
argument_list|()
argument_list|,
name|o2
operator|.
name|getObject
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|successor (Element<E> o1, Element<E> o2)
specifier|public
name|boolean
name|successor
parameter_list|(
name|Element
argument_list|<
name|E
argument_list|>
name|o1
parameter_list|,
name|Element
argument_list|<
name|E
argument_list|>
name|o2
parameter_list|)
block|{
return|return
name|comparator
operator|.
name|successor
argument_list|(
name|o1
operator|.
name|getObject
argument_list|()
argument_list|,
name|o2
operator|.
name|getObject
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|compare (Element<E> o1, Element<E> o2)
specifier|public
name|int
name|compare
parameter_list|(
name|Element
argument_list|<
name|E
argument_list|>
name|o1
parameter_list|,
name|Element
argument_list|<
name|E
argument_list|>
name|o2
parameter_list|)
block|{
return|return
name|comparator
operator|.
name|compare
argument_list|(
name|o1
operator|.
name|getObject
argument_list|()
argument_list|,
name|o2
operator|.
name|getObject
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isValid (Element<E> o1)
specifier|public
name|boolean
name|isValid
parameter_list|(
name|Element
argument_list|<
name|E
argument_list|>
name|o1
parameter_list|)
block|{
return|return
name|comparator
operator|.
name|isValid
argument_list|(
name|o1
operator|.
name|getObject
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|comparator
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

