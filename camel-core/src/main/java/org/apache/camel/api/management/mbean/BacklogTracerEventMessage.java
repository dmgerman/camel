begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.api.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|api
operator|.
name|management
operator|.
name|mbean
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_comment
comment|/**  * Represents a traced message by the {@link org.apache.camel.processor.interceptor.BacklogTracer}.  */
end_comment

begin_interface
DECL|interface|BacklogTracerEventMessage
specifier|public
interface|interface
name|BacklogTracerEventMessage
extends|extends
name|Serializable
block|{
DECL|field|ROOT_TAG
name|String
name|ROOT_TAG
init|=
literal|"backlogTracerEventMessage"
decl_stmt|;
DECL|field|TIMESTAMP_FORMAT
name|String
name|TIMESTAMP_FORMAT
init|=
literal|"yyyy-MM-dd'T'HH:mm:ss.SSSZ"
decl_stmt|;
DECL|method|getUid ()
name|long
name|getUid
parameter_list|()
function_decl|;
DECL|method|getTimestamp ()
name|Date
name|getTimestamp
parameter_list|()
function_decl|;
DECL|method|getToNode ()
name|String
name|getToNode
parameter_list|()
function_decl|;
DECL|method|getExchangeId ()
name|String
name|getExchangeId
parameter_list|()
function_decl|;
DECL|method|getMessageAsXml ()
name|String
name|getMessageAsXml
parameter_list|()
function_decl|;
comment|/**      * Dumps the event message as XML using the {@link #ROOT_TAG} as root tag.      *<p/>      * The<tt>timestamp</tt> tag is formatted in the format defined by {@link #TIMESTAMP_FORMAT}      *      * @param indent number of spaces to indent      * @return xml representation of this event      */
DECL|method|toXml (int indent)
name|String
name|toXml
parameter_list|(
name|int
name|indent
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

