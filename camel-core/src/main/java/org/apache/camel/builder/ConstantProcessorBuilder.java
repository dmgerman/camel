begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|Processor
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
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|ConstantProcessorBuilder
specifier|public
class|class
name|ConstantProcessorBuilder
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
implements|implements
name|ProcessorFactory
argument_list|<
name|E
argument_list|>
block|{
DECL|field|processor
specifier|private
name|Processor
argument_list|<
name|E
argument_list|>
name|processor
decl_stmt|;
DECL|method|ConstantProcessorBuilder (Processor<E> processor)
specifier|public
name|ConstantProcessorBuilder
parameter_list|(
name|Processor
argument_list|<
name|E
argument_list|>
name|processor
parameter_list|)
block|{
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
block|}
DECL|method|createProcessor ()
specifier|public
name|Processor
argument_list|<
name|E
argument_list|>
name|createProcessor
parameter_list|()
block|{
return|return
name|processor
return|;
block|}
block|}
end_class

end_unit

