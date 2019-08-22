begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ErrorHandlerFactory
import|;
end_import

begin_comment
comment|/**  * A builder of a<a href="http://camel.apache.org/error-handler.html">Error  * Handler</a>  */
end_comment

begin_interface
DECL|interface|ErrorHandlerBuilder
specifier|public
interface|interface
name|ErrorHandlerBuilder
extends|extends
name|ErrorHandlerFactory
block|{
comment|/**      * Whether this error handler supports transacted exchanges.      */
DECL|method|supportTransacted ()
name|boolean
name|supportTransacted
parameter_list|()
function_decl|;
comment|/**      * Clones this builder so each {@link RouteBuilder} has its private builder      * to use, to avoid changes from one {@link RouteBuilder} to influence the      * others.      *<p/>      * This is needed by the current Camel 2.x architecture.      *      * @return a clone of this {@link ErrorHandlerBuilder}      */
DECL|method|cloneBuilder ()
name|ErrorHandlerBuilder
name|cloneBuilder
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

