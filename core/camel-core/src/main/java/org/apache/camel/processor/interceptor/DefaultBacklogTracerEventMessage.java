begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.interceptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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

begin_import
import|import
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
operator|.
name|BacklogTracerEventMessage
import|;
end_import

begin_comment
comment|/**  * An event message holding the traced message by the {@link BacklogTracer}.  */
end_comment

begin_class
DECL|class|DefaultBacklogTracerEventMessage
specifier|public
specifier|final
class|class
name|DefaultBacklogTracerEventMessage
implements|implements
name|BacklogTracerEventMessage
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|uid
specifier|private
specifier|final
name|long
name|uid
decl_stmt|;
DECL|field|timestamp
specifier|private
specifier|final
name|Date
name|timestamp
decl_stmt|;
DECL|field|routeId
specifier|private
specifier|final
name|String
name|routeId
decl_stmt|;
DECL|field|toNode
specifier|private
specifier|final
name|String
name|toNode
decl_stmt|;
DECL|field|exchangeId
specifier|private
specifier|final
name|String
name|exchangeId
decl_stmt|;
DECL|field|messageAsXml
specifier|private
specifier|final
name|String
name|messageAsXml
decl_stmt|;
DECL|method|DefaultBacklogTracerEventMessage (long uid, Date timestamp, String routeId, String toNode, String exchangeId, String messageAsXml)
specifier|public
name|DefaultBacklogTracerEventMessage
parameter_list|(
name|long
name|uid
parameter_list|,
name|Date
name|timestamp
parameter_list|,
name|String
name|routeId
parameter_list|,
name|String
name|toNode
parameter_list|,
name|String
name|exchangeId
parameter_list|,
name|String
name|messageAsXml
parameter_list|)
block|{
name|this
operator|.
name|uid
operator|=
name|uid
expr_stmt|;
name|this
operator|.
name|timestamp
operator|=
name|timestamp
expr_stmt|;
name|this
operator|.
name|routeId
operator|=
name|routeId
expr_stmt|;
name|this
operator|.
name|toNode
operator|=
name|toNode
expr_stmt|;
name|this
operator|.
name|exchangeId
operator|=
name|exchangeId
expr_stmt|;
name|this
operator|.
name|messageAsXml
operator|=
name|messageAsXml
expr_stmt|;
block|}
DECL|method|getUid ()
specifier|public
name|long
name|getUid
parameter_list|()
block|{
return|return
name|uid
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
DECL|method|getToNode ()
specifier|public
name|String
name|getToNode
parameter_list|()
block|{
return|return
name|toNode
return|;
block|}
DECL|method|getExchangeId ()
specifier|public
name|String
name|getExchangeId
parameter_list|()
block|{
return|return
name|exchangeId
return|;
block|}
DECL|method|getMessageAsXml ()
specifier|public
name|String
name|getMessageAsXml
parameter_list|()
block|{
return|return
name|messageAsXml
return|;
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
literal|"DefaultBacklogTracerEventMessage["
operator|+
name|exchangeId
operator|+
literal|" at "
operator|+
name|toNode
operator|+
literal|"]"
return|;
block|}
comment|/**      * Dumps the event message as XML using the {@link #ROOT_TAG} as root tag.      *<p/>      * The<tt>timestamp</tt> tag is formatted in the format defined by {@link #TIMESTAMP_FORMAT}      *      * @return xml representation of this event      */
DECL|method|toXml (int indent)
specifier|public
name|String
name|toXml
parameter_list|(
name|int
name|indent
parameter_list|)
block|{
name|StringBuilder
name|prefix
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|indent
condition|;
name|i
operator|++
control|)
block|{
name|prefix
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|prefix
argument_list|)
operator|.
name|append
argument_list|(
literal|"<"
argument_list|)
operator|.
name|append
argument_list|(
name|ROOT_TAG
argument_list|)
operator|.
name|append
argument_list|(
literal|">\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|prefix
argument_list|)
operator|.
name|append
argument_list|(
literal|"<uid>"
argument_list|)
operator|.
name|append
argument_list|(
name|uid
argument_list|)
operator|.
name|append
argument_list|(
literal|"</uid>\n"
argument_list|)
expr_stmt|;
name|String
name|ts
init|=
operator|new
name|SimpleDateFormat
argument_list|(
name|TIMESTAMP_FORMAT
argument_list|)
operator|.
name|format
argument_list|(
name|timestamp
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|prefix
argument_list|)
operator|.
name|append
argument_list|(
literal|"<timestamp>"
argument_list|)
operator|.
name|append
argument_list|(
name|ts
argument_list|)
operator|.
name|append
argument_list|(
literal|"</timestamp>\n"
argument_list|)
expr_stmt|;
comment|// route id is optional and we then use an empty value for no route id
name|sb
operator|.
name|append
argument_list|(
name|prefix
argument_list|)
operator|.
name|append
argument_list|(
literal|"<routeId>"
argument_list|)
operator|.
name|append
argument_list|(
name|routeId
operator|!=
literal|null
condition|?
name|routeId
else|:
literal|""
argument_list|)
operator|.
name|append
argument_list|(
literal|"</routeId>\n"
argument_list|)
expr_stmt|;
if|if
condition|(
name|toNode
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|prefix
argument_list|)
operator|.
name|append
argument_list|(
literal|"<toNode>"
argument_list|)
operator|.
name|append
argument_list|(
name|toNode
argument_list|)
operator|.
name|append
argument_list|(
literal|"</toNode>\n"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// if first message the use routeId as toNode
name|sb
operator|.
name|append
argument_list|(
name|prefix
argument_list|)
operator|.
name|append
argument_list|(
literal|"<toNode>"
argument_list|)
operator|.
name|append
argument_list|(
name|routeId
argument_list|)
operator|.
name|append
argument_list|(
literal|"</toNode>\n"
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|prefix
argument_list|)
operator|.
name|append
argument_list|(
literal|"<exchangeId>"
argument_list|)
operator|.
name|append
argument_list|(
name|exchangeId
argument_list|)
operator|.
name|append
argument_list|(
literal|"</exchangeId>\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|prefix
argument_list|)
operator|.
name|append
argument_list|(
name|messageAsXml
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|prefix
argument_list|)
operator|.
name|append
argument_list|(
literal|"</"
argument_list|)
operator|.
name|append
argument_list|(
name|ROOT_TAG
argument_list|)
operator|.
name|append
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit
