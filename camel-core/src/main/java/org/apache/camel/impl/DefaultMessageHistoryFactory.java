begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p>  * http://www.apache.org/licenses/LICENSE-2.0  *<p>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|MessageHistory
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
name|NamedNode
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
name|MessageHistoryFactory
import|;
end_import

begin_class
DECL|class|DefaultMessageHistoryFactory
specifier|public
class|class
name|DefaultMessageHistoryFactory
implements|implements
name|MessageHistoryFactory
block|{
annotation|@
name|Override
DECL|method|newMessageHistory (String routeId, NamedNode node, Date timestamp)
specifier|public
name|MessageHistory
name|newMessageHistory
parameter_list|(
name|String
name|routeId
parameter_list|,
name|NamedNode
name|node
parameter_list|,
name|Date
name|timestamp
parameter_list|)
block|{
return|return
operator|new
name|DefaultMessageHistory
argument_list|(
name|routeId
argument_list|,
name|node
argument_list|,
name|timestamp
argument_list|)
return|;
block|}
block|}
end_class

end_unit

