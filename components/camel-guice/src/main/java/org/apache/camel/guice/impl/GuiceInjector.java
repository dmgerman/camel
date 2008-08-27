begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.guice.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|guice
operator|.
name|impl
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
name|spi
operator|.
name|Injector
import|;
end_import

begin_comment
comment|/**  * An injector which uses Guice to perform the dependency injection of types  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|GuiceInjector
specifier|public
class|class
name|GuiceInjector
implements|implements
name|Injector
block|{
DECL|field|injector
specifier|private
specifier|final
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Injector
name|injector
decl_stmt|;
DECL|method|GuiceInjector (com.google.inject.Injector injector)
specifier|public
name|GuiceInjector
parameter_list|(
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Injector
name|injector
parameter_list|)
block|{
name|this
operator|.
name|injector
operator|=
name|injector
expr_stmt|;
block|}
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
return|return
name|injector
operator|.
name|getInstance
argument_list|(
name|type
argument_list|)
return|;
block|}
block|}
end_class

end_unit

