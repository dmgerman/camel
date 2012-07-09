begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter.producer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|twitter
operator|.
name|producer
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
name|component
operator|.
name|twitter
operator|.
name|TwitterEndpoint
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
name|impl
operator|.
name|DefaultProducer
import|;
end_import

begin_comment
comment|/**  * Abstracts common producer capabilities.  */
end_comment

begin_class
DECL|class|Twitter4JProducer
specifier|public
specifier|abstract
class|class
name|Twitter4JProducer
extends|extends
name|DefaultProducer
block|{
comment|/**      * Instance of TwitterEndpoint.      */
DECL|field|te
specifier|protected
name|TwitterEndpoint
name|te
decl_stmt|;
DECL|method|Twitter4JProducer (TwitterEndpoint te)
specifier|protected
name|Twitter4JProducer
parameter_list|(
name|TwitterEndpoint
name|te
parameter_list|)
block|{
name|super
argument_list|(
name|te
argument_list|)
expr_stmt|;
name|this
operator|.
name|te
operator|=
name|te
expr_stmt|;
block|}
block|}
end_class

end_unit

