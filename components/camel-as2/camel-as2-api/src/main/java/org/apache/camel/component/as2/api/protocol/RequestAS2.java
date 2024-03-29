begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api.protocol
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|protocol
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|AS2ClientManager
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
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|AS2Constants
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
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|AS2Header
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
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|InvalidAS2NameException
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
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|util
operator|.
name|AS2Utils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpRequestInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|HttpContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|HttpCoreContext
import|;
end_import

begin_class
DECL|class|RequestAS2
specifier|public
class|class
name|RequestAS2
implements|implements
name|HttpRequestInterceptor
block|{
DECL|field|as2Version
specifier|private
specifier|final
name|String
name|as2Version
decl_stmt|;
DECL|field|clientFQDN
specifier|private
specifier|final
name|String
name|clientFQDN
decl_stmt|;
DECL|method|RequestAS2 (String as2Version, String clientFQDN)
specifier|public
name|RequestAS2
parameter_list|(
name|String
name|as2Version
parameter_list|,
name|String
name|clientFQDN
parameter_list|)
block|{
name|this
operator|.
name|as2Version
operator|=
name|as2Version
expr_stmt|;
name|this
operator|.
name|clientFQDN
operator|=
name|clientFQDN
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (HttpRequest request, HttpContext context)
specifier|public
name|void
name|process
parameter_list|(
name|HttpRequest
name|request
parameter_list|,
name|HttpContext
name|context
parameter_list|)
throws|throws
name|HttpException
throws|,
name|IOException
block|{
name|HttpCoreContext
name|coreContext
init|=
name|HttpCoreContext
operator|.
name|adapt
argument_list|(
name|context
argument_list|)
decl_stmt|;
comment|/* MIME header */
name|request
operator|.
name|addHeader
argument_list|(
name|AS2Header
operator|.
name|MIME_VERSION
argument_list|,
name|AS2Constants
operator|.
name|MIME_VERSION
argument_list|)
expr_stmt|;
comment|/* Subject header */
name|String
name|subject
init|=
name|coreContext
operator|.
name|getAttribute
argument_list|(
name|AS2ClientManager
operator|.
name|SUBJECT
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|request
operator|.
name|addHeader
argument_list|(
name|AS2Header
operator|.
name|SUBJECT
argument_list|,
name|subject
argument_list|)
expr_stmt|;
comment|/* From header */
name|String
name|from
init|=
name|coreContext
operator|.
name|getAttribute
argument_list|(
name|AS2ClientManager
operator|.
name|FROM
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|request
operator|.
name|addHeader
argument_list|(
name|AS2Header
operator|.
name|FROM
argument_list|,
name|from
argument_list|)
expr_stmt|;
comment|/* AS2-Version header */
name|request
operator|.
name|addHeader
argument_list|(
name|AS2Header
operator|.
name|AS2_VERSION
argument_list|,
name|as2Version
argument_list|)
expr_stmt|;
comment|/* AS2-From header */
name|String
name|as2From
init|=
name|coreContext
operator|.
name|getAttribute
argument_list|(
name|AS2ClientManager
operator|.
name|AS2_FROM
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|AS2Utils
operator|.
name|validateAS2Name
argument_list|(
name|as2From
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidAS2NameException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|HttpException
argument_list|(
literal|"Invalid AS-From name"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|request
operator|.
name|addHeader
argument_list|(
name|AS2Header
operator|.
name|AS2_FROM
argument_list|,
name|as2From
argument_list|)
expr_stmt|;
comment|/* AS2-To header */
name|String
name|as2To
init|=
name|coreContext
operator|.
name|getAttribute
argument_list|(
name|AS2ClientManager
operator|.
name|AS2_TO
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|AS2Utils
operator|.
name|validateAS2Name
argument_list|(
name|as2To
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidAS2NameException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|HttpException
argument_list|(
literal|"Invalid AS-To name"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|request
operator|.
name|addHeader
argument_list|(
name|AS2Header
operator|.
name|AS2_TO
argument_list|,
name|as2To
argument_list|)
expr_stmt|;
comment|/* Message-Id header*/
comment|// SHOULD be set to aid in message reconciliation
name|request
operator|.
name|addHeader
argument_list|(
name|AS2Header
operator|.
name|MESSAGE_ID
argument_list|,
name|AS2Utils
operator|.
name|createMessageId
argument_list|(
name|clientFQDN
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

