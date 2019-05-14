begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|RuntimeExchangeException
import|;
end_import

begin_comment
comment|/**  * An exception thrown if an attempted method invocation resulted in an ambiguous method  * such that multiple methods match the inbound message exchange  */
end_comment

begin_class
DECL|class|AmbiguousMethodCallException
specifier|public
class|class
name|AmbiguousMethodCallException
extends|extends
name|RuntimeExchangeException
block|{
DECL|field|methods
specifier|private
specifier|final
name|Collection
argument_list|<
name|MethodInfo
argument_list|>
name|methods
decl_stmt|;
DECL|method|AmbiguousMethodCallException (Exchange exchange, Collection<MethodInfo> methods)
specifier|public
name|AmbiguousMethodCallException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Collection
argument_list|<
name|MethodInfo
argument_list|>
name|methods
parameter_list|)
block|{
name|super
argument_list|(
literal|"Ambiguous method invocations possible: "
operator|+
name|methods
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|this
operator|.
name|methods
operator|=
name|methods
expr_stmt|;
block|}
comment|/**      * The ambiguous methods for which a single method could not be chosen      */
DECL|method|getMethods ()
specifier|public
name|Collection
argument_list|<
name|MethodInfo
argument_list|>
name|getMethods
parameter_list|()
block|{
return|return
name|methods
return|;
block|}
block|}
end_class

end_unit

