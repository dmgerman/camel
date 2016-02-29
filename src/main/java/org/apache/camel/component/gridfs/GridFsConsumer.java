begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gridfs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gridfs
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
name|impl
operator|.
name|DefaultConsumer
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
DECL|class|GridFsConsumer
specifier|public
class|class
name|GridFsConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|ep
specifier|final
name|GridFsEndpoint
name|ep
decl_stmt|;
comment|/**      * @param endpoint      * @param processor      */
DECL|method|GridFsConsumer (GridFsEndpoint endpoint, Processor processor)
specifier|public
name|GridFsConsumer
parameter_list|(
name|GridFsEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|ep
operator|=
name|endpoint
expr_stmt|;
block|}
block|}
end_class

end_unit

