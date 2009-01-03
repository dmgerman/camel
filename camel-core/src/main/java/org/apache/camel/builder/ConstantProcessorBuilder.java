begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * A builder wrapping a {@link Processor}.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ConstantProcessorBuilder
specifier|public
class|class
name|ConstantProcessorBuilder
implements|implements
name|ProcessorFactory
block|{
DECL|field|processor
specifier|private
name|Processor
name|processor
decl_stmt|;
DECL|method|ConstantProcessorBuilder (Processor processor)
specifier|public
name|ConstantProcessorBuilder
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|processor
argument_list|,
literal|"processor"
argument_list|)
expr_stmt|;
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
name|createProcessor
parameter_list|()
block|{
return|return
name|processor
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
name|processor
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

