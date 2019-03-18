begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|sample.camel
package|package
name|sample
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Consume
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
name|Produce
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Component
import|;
end_import

begin_comment
comment|/**  * A POJO that listen to messages from the seda:numbers endpoint via {@link Consume}  * and then via {@link MagicNumber} and {@link Produce} sends a message that will  * be printed in the console.  */
end_comment

begin_class
annotation|@
name|Component
DECL|class|NumberPojo
specifier|public
class|class
name|NumberPojo
block|{
comment|// sends the message to the stream:out endpoint but hidden behind this interface
comment|// so the client java code below can use the interface method instead of Camel's
comment|// FluentProducerTemplate or ProducerTemplate APIs
annotation|@
name|Produce
argument_list|(
literal|"stream:out"
argument_list|)
DECL|field|magic
specifier|private
name|MagicNumber
name|magic
decl_stmt|;
comment|// only consume when the predicate matches, eg when the message body is lower than 100
annotation|@
name|Consume
argument_list|(
name|value
operator|=
literal|"direct:numbers"
argument_list|,
name|predicate
operator|=
literal|"${body}< 100"
argument_list|)
DECL|method|lowNumber (int number)
specifier|public
name|void
name|lowNumber
parameter_list|(
name|int
name|number
parameter_list|)
block|{
name|magic
operator|.
name|onMagicNumber
argument_list|(
literal|"Got a low number "
operator|+
name|number
argument_list|)
expr_stmt|;
block|}
comment|// only consume when the predicate matches, eg when the message body is higher or equal to 100
annotation|@
name|Consume
argument_list|(
name|value
operator|=
literal|"direct:numbers"
argument_list|,
name|predicate
operator|=
literal|"${body}>= 100"
argument_list|)
DECL|method|highNumber (int number)
specifier|public
name|void
name|highNumber
parameter_list|(
name|int
name|number
parameter_list|)
block|{
name|magic
operator|.
name|onMagicNumber
argument_list|(
literal|"Got a high number "
operator|+
name|number
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

