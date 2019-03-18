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
name|AS2Header
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|util
operator|.
name|CharArrayBuffer
import|;
end_import

begin_class
DECL|class|RequestMDN
specifier|public
class|class
name|RequestMDN
implements|implements
name|HttpRequestInterceptor
block|{
DECL|field|SIGNED_RECEIPT_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|SIGNED_RECEIPT_PREFIX
init|=
literal|"signed-receipt-protocol=optional, pkcs7-signature; signed-receipt-micalg=optional"
decl_stmt|;
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
comment|/* Disposition-Notification-To */
name|String
name|dispositionNotificationTo
init|=
name|coreContext
operator|.
name|getAttribute
argument_list|(
name|AS2ClientManager
operator|.
name|DISPOSITION_NOTIFICATION_TO
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|dispositionNotificationTo
operator|!=
literal|null
condition|)
block|{
name|request
operator|.
name|addHeader
argument_list|(
name|AS2Header
operator|.
name|DISPOSITION_NOTIFICATION_TO
argument_list|,
name|dispositionNotificationTo
argument_list|)
expr_stmt|;
name|String
index|[]
name|micAlgorithms
init|=
name|coreContext
operator|.
name|getAttribute
argument_list|(
name|AS2ClientManager
operator|.
name|SIGNED_RECEIPT_MIC_ALGORITHMS
argument_list|,
name|String
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|micAlgorithms
operator|==
literal|null
condition|)
block|{
comment|// requesting unsigned receipt: indicate by not setting Disposition-Notification-Options header
block|}
else|else
block|{
name|CharArrayBuffer
name|options
init|=
operator|new
name|CharArrayBuffer
argument_list|(
name|SIGNED_RECEIPT_PREFIX
operator|.
name|length
argument_list|()
operator|+
literal|5
operator|*
name|micAlgorithms
operator|.
name|length
argument_list|)
decl_stmt|;
name|options
operator|.
name|append
argument_list|(
name|SIGNED_RECEIPT_PREFIX
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|micAlgorithm
range|:
name|micAlgorithms
control|)
block|{
name|options
operator|.
name|append
argument_list|(
literal|","
operator|+
name|micAlgorithm
argument_list|)
expr_stmt|;
block|}
name|request
operator|.
name|addHeader
argument_list|(
name|AS2Header
operator|.
name|DISPOSITION_NOTIFICATION_OPTIONS
argument_list|,
name|options
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

