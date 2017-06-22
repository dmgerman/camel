begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.rx.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|rx
operator|.
name|support
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
name|CamelContext
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
name|Injector
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
name|ReflectionInjector
import|;
end_import

begin_class
DECL|class|ReactiveInjector
specifier|public
class|class
name|ReactiveInjector
implements|implements
name|Injector
block|{
comment|// use the reflection injector
DECL|field|delegate
specifier|private
specifier|final
name|Injector
name|delegate
init|=
operator|new
name|ReflectionInjector
argument_list|()
decl_stmt|;
DECL|field|postProcessor
specifier|private
specifier|final
name|ReactiveBeanPostProcessor
name|postProcessor
decl_stmt|;
DECL|method|ReactiveInjector (CamelContext context)
specifier|public
name|ReactiveInjector
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|postProcessor
operator|=
operator|new
name|ReactiveBeanPostProcessor
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newInstance (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|newInstance
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|T
name|answer
init|=
name|delegate
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|postProcessor
operator|.
name|postProcessBeforeInitialization
argument_list|(
name|answer
argument_list|,
name|answer
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|postProcessor
operator|.
name|postProcessAfterInitialization
argument_list|(
name|answer
argument_list|,
name|answer
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Error during post processing of bean "
operator|+
name|answer
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|newInstance (Class<T> type, Object instance)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|newInstance
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|instance
parameter_list|)
block|{
name|T
name|answer
init|=
name|delegate
operator|.
name|newInstance
argument_list|(
name|type
argument_list|,
name|instance
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|postProcessor
operator|.
name|postProcessBeforeInitialization
argument_list|(
name|answer
argument_list|,
name|answer
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|postProcessor
operator|.
name|postProcessAfterInitialization
argument_list|(
name|answer
argument_list|,
name|answer
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Error during post processing of bean "
operator|+
name|answer
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|supportsAutoWiring ()
specifier|public
name|boolean
name|supportsAutoWiring
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|supportsAutoWiring
argument_list|()
return|;
block|}
block|}
end_class

end_unit

