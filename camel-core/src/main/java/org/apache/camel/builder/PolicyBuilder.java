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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|RuntimeCamelException
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
name|spi
operator|.
name|Policy
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 519943 $  */
end_comment

begin_class
DECL|class|PolicyBuilder
specifier|public
class|class
name|PolicyBuilder
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
DECL|field|policies
specifier|private
specifier|final
name|ArrayList
argument_list|<
name|Policy
argument_list|<
name|E
argument_list|>
argument_list|>
name|policies
init|=
operator|new
name|ArrayList
argument_list|<
name|Policy
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|parent
specifier|private
specifier|final
name|FromBuilder
argument_list|<
name|E
argument_list|>
name|parent
decl_stmt|;
DECL|field|target
specifier|private
name|FromBuilder
argument_list|<
name|E
argument_list|>
name|target
decl_stmt|;
DECL|method|PolicyBuilder (FromBuilder<E> parent)
specifier|public
name|PolicyBuilder
parameter_list|(
name|FromBuilder
argument_list|<
name|E
argument_list|>
name|parent
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
block|}
annotation|@
name|Fluent
argument_list|(
literal|"policy"
argument_list|)
DECL|method|add (@luentArgR) Policy<E> interceptor)
specifier|public
name|PolicyBuilder
argument_list|<
name|E
argument_list|>
name|add
parameter_list|(
annotation|@
name|FluentArg
argument_list|(
literal|"ref"
argument_list|)
name|Policy
argument_list|<
name|E
argument_list|>
name|interceptor
parameter_list|)
block|{
name|policies
operator|.
name|add
argument_list|(
name|interceptor
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Fluent
argument_list|(
name|callOnElementEnd
operator|=
literal|true
argument_list|)
DECL|method|target ()
specifier|public
name|FromBuilder
argument_list|<
name|E
argument_list|>
name|target
parameter_list|()
block|{
name|this
operator|.
name|target
operator|=
operator|new
name|FromBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|parent
argument_list|)
expr_stmt|;
return|return
name|target
return|;
block|}
DECL|method|createProcessor ()
specifier|public
name|Processor
argument_list|<
name|E
argument_list|>
name|createProcessor
parameter_list|()
throws|throws
name|Exception
block|{
comment|// The target is required.
if|if
condition|(
name|target
operator|==
literal|null
condition|)
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"target not provided."
argument_list|)
throw|;
name|Processor
argument_list|<
name|E
argument_list|>
name|last
init|=
name|target
operator|.
name|createProcessor
argument_list|()
decl_stmt|;
name|Collections
operator|.
name|reverse
argument_list|(
name|policies
argument_list|)
expr_stmt|;
for|for
control|(
name|Policy
argument_list|<
name|E
argument_list|>
name|p
range|:
name|policies
control|)
block|{
name|last
operator|=
name|p
operator|.
name|wrap
argument_list|(
name|last
argument_list|)
expr_stmt|;
block|}
return|return
name|last
return|;
block|}
block|}
end_class

end_unit

