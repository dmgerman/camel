begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.bam.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|bam
operator|.
name|processor
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
name|CamelExchangeException
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
name|Exchange
import|;
end_import

begin_comment
comment|/**  * An exception thrown if no correlation key could be found for a message  * exchange preventing any particular orchestration or  *<a href="http://camel.apache.org/bam.html">BAM</a>  *  * @version   */
end_comment

begin_class
DECL|class|NoCorrelationKeyException
specifier|public
class|class
name|NoCorrelationKeyException
extends|extends
name|CamelExchangeException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|4511220911189364989L
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|BamProcessorSupport
argument_list|<
name|?
argument_list|>
name|processor
decl_stmt|;
DECL|method|NoCorrelationKeyException (BamProcessorSupport<?> processor, Exchange exchange)
specifier|public
name|NoCorrelationKeyException
parameter_list|(
name|BamProcessorSupport
argument_list|<
name|?
argument_list|>
name|processor
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|super
argument_list|(
literal|"No correlation key could be found for "
operator|+
name|processor
operator|.
name|getCorrelationKeyExpression
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
block|}
DECL|method|getProcessor ()
specifier|public
name|BamProcessorSupport
argument_list|<
name|?
argument_list|>
name|getProcessor
parameter_list|()
block|{
return|return
name|processor
return|;
block|}
block|}
end_class

end_unit

