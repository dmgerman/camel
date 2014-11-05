begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|StopWatch
import|;
end_import

begin_comment
comment|/**  * Default {@link org.apache.camel.MessageHistory}.  */
end_comment

begin_class
DECL|class|DefaultMessageHistory
specifier|public
class|class
name|DefaultMessageHistory
implements|implements
name|MessageHistory
block|{
DECL|field|routeId
specifier|private
specifier|final
name|String
name|routeId
decl_stmt|;
DECL|field|node
specifier|private
specifier|final
name|NamedNode
name|node
decl_stmt|;
DECL|field|nodeId
specifier|private
specifier|final
name|String
name|nodeId
decl_stmt|;
DECL|field|timestamp
specifier|private
specifier|final
name|Date
name|timestamp
decl_stmt|;
DECL|field|stopWatch
specifier|private
specifier|final
name|StopWatch
name|stopWatch
decl_stmt|;
DECL|method|DefaultMessageHistory (String routeId, NamedNode node, Date timestamp)
specifier|public
name|DefaultMessageHistory
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
name|this
operator|.
name|routeId
operator|=
name|routeId
expr_stmt|;
name|this
operator|.
name|node
operator|=
name|node
expr_stmt|;
name|this
operator|.
name|nodeId
operator|=
name|node
operator|.
name|getId
argument_list|()
expr_stmt|;
name|this
operator|.
name|timestamp
operator|=
name|timestamp
expr_stmt|;
name|this
operator|.
name|stopWatch
operator|=
operator|new
name|StopWatch
argument_list|()
expr_stmt|;
block|}
DECL|method|getRouteId ()
specifier|public
name|String
name|getRouteId
parameter_list|()
block|{
return|return
name|routeId
return|;
block|}
DECL|method|getNode ()
specifier|public
name|NamedNode
name|getNode
parameter_list|()
block|{
return|return
name|node
return|;
block|}
DECL|method|getTimestamp ()
specifier|public
name|Date
name|getTimestamp
parameter_list|()
block|{
return|return
name|timestamp
return|;
block|}
DECL|method|getElapsed ()
specifier|public
name|long
name|getElapsed
parameter_list|()
block|{
return|return
name|stopWatch
operator|.
name|taken
argument_list|()
return|;
block|}
DECL|method|nodeProcessingDone ()
specifier|public
name|void
name|nodeProcessingDone
parameter_list|()
block|{
name|stopWatch
operator|.
name|stop
argument_list|()
expr_stmt|;
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
literal|"DefaultMessageHistory["
operator|+
literal|"routeId="
operator|+
name|routeId
operator|+
literal|", node="
operator|+
name|nodeId
operator|+
literal|']'
return|;
block|}
block|}
end_class

end_unit

