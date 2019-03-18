begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Constants used in Camel XMPP module  */
end_comment

begin_interface
DECL|interface|XmppConstants
specifier|public
interface|interface
name|XmppConstants
block|{
DECL|field|MESSAGE_TYPE
name|String
name|MESSAGE_TYPE
init|=
literal|"CamelXmppMessageType"
decl_stmt|;
DECL|field|SUBJECT
name|String
name|SUBJECT
init|=
literal|"CamelXmppSubject"
decl_stmt|;
DECL|field|THREAD_ID
name|String
name|THREAD_ID
init|=
literal|"CamelXmppThreadID"
decl_stmt|;
DECL|field|FROM
name|String
name|FROM
init|=
literal|"CamelXmppFrom"
decl_stmt|;
comment|/**      * @deprecated use {@link #STANZA_ID} instead.      */
annotation|@
name|Deprecated
DECL|field|PACKET_ID
name|String
name|PACKET_ID
init|=
literal|"CamelXmppPacketID"
decl_stmt|;
DECL|field|STANZA_ID
name|String
name|STANZA_ID
init|=
literal|"CamelXmppStanzaID"
decl_stmt|;
DECL|field|TO
name|String
name|TO
init|=
literal|"CamelXmppTo"
decl_stmt|;
DECL|field|DOC_HEADER
name|String
name|DOC_HEADER
init|=
literal|"CamelXmppDoc"
decl_stmt|;
block|}
end_interface

end_unit

