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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ContextTestSupport
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
name|URISupport
import|;
end_import

begin_comment
comment|/**  * @version  */
end_comment

begin_class
DECL|class|DefaultEndpointTest
specifier|public
class|class
name|DefaultEndpointTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testSanitizeUri ()
specifier|public
name|void
name|testSanitizeUri
parameter_list|()
block|{
name|assertNull
argument_list|(
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertSanitizedUriUnchanged
argument_list|(
literal|"http://camel.apache.org"
argument_list|)
expr_stmt|;
name|assertSanitizedUriUnchanged
argument_list|(
literal|"irc://irc.codehaus.org/camel"
argument_list|)
expr_stmt|;
name|assertSanitizedUriUnchanged
argument_list|(
literal|"direct:foo?bar=123&cheese=yes"
argument_list|)
expr_stmt|;
name|assertSanitizedUriUnchanged
argument_list|(
literal|"https://issues.apache.org/activemq/secure/AddComment!default.jspa?id=33239"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ftp://host.mysite.com/records?passiveMode=true&user=someuser&password=******"
argument_list|,
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
literal|"ftp://host.mysite.com/records?passiveMode=true&user=someuser&password=superSecret"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"sftp://host.mysite.com/records?user=someuser&privateKeyFile=key.file&privateKeyFilePassphrase=******&knownHostsFile=hosts.list"
argument_list|,
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
literal|"sftp://host.mysite.com/records?user=someuser&privateKeyFile=key.file&privateKeyFilePassphrase=superSecret&knownHostsFile=hosts.list"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"aws-sqs://MyQueue?accessKey=1672t4rflhnhli3&secretKey=******"
argument_list|,
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
literal|"aws-sqs://MyQueue?accessKey=1672t4rflhnhli3&secretKey=qi472qfberu33dqjncq"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Ensures that the Uri was not changed because no password was found.      *      * @param uri The uri to test.      */
DECL|method|assertSanitizedUriUnchanged (String uri)
specifier|private
name|void
name|assertSanitizedUriUnchanged
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|uri
argument_list|,
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

