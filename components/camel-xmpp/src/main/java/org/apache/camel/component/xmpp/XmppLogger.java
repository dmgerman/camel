begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xmpp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xmpp
package|;
end_package

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|PacketListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|packet
operator|.
name|Packet
import|;
end_import

begin_class
DECL|class|XmppLogger
specifier|public
class|class
name|XmppLogger
implements|implements
name|PacketListener
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|XmppLogger
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|direction
specifier|private
name|String
name|direction
decl_stmt|;
DECL|method|XmppLogger (String direction)
specifier|public
name|XmppLogger
parameter_list|(
name|String
name|direction
parameter_list|)
block|{
name|this
operator|.
name|direction
operator|=
name|direction
expr_stmt|;
block|}
DECL|method|processPacket (Packet packet)
specifier|public
name|void
name|processPacket
parameter_list|(
name|Packet
name|packet
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|direction
operator|+
literal|" : "
operator|+
name|packet
operator|.
name|toXML
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

