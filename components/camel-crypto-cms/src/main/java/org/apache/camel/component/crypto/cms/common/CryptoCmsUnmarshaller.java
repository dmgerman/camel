begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.crypto.cms.common
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|crypto
operator|.
name|cms
operator|.
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|Exchange
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
name|Message
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
name|util
operator|.
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|codec
operator|.
name|binary
operator|.
name|Base64InputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cert
operator|.
name|X509CertificateHolder
import|;
end_import

begin_class
DECL|class|CryptoCmsUnmarshaller
specifier|public
specifier|abstract
class|class
name|CryptoCmsUnmarshaller
implements|implements
name|Processor
block|{
DECL|field|config
specifier|private
specifier|final
name|CryptoCmsUnMarshallerConfiguration
name|config
decl_stmt|;
DECL|method|CryptoCmsUnmarshaller (CryptoCmsUnMarshallerConfiguration config)
specifier|public
name|CryptoCmsUnmarshaller
parameter_list|(
name|CryptoCmsUnMarshallerConfiguration
name|config
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
comment|// @Override
DECL|method|getConfiguration ()
specifier|public
name|CryptoCmsUnMarshallerConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|config
return|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// NOPMD all
comment|// exceptions must
comment|// be caught to
comment|// react on
comment|// exception case
comment|// and re-thrown,
comment|// see code below
name|InputStream
name|stream
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
comment|// lets setup the out message before we invoke the dataFormat
comment|// so that it can mutate it if necessary
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|out
operator|.
name|copyFrom
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|isFromBase64
argument_list|()
condition|)
block|{
name|stream
operator|=
operator|new
name|Base64InputStream
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
name|Object
name|result
init|=
name|unmarshalInternal
argument_list|(
name|stream
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|out
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// remove OUT message, as an exception occurred
name|exchange
operator|.
name|setOut
argument_list|(
literal|null
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|stream
argument_list|,
literal|"input stream"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|unmarshalInternal (InputStream is, Exchange exchange)
specifier|protected
specifier|abstract
name|Object
name|unmarshalInternal
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|certsToString (Collection<X509Certificate> certs)
specifier|protected
name|String
name|certsToString
parameter_list|(
name|Collection
argument_list|<
name|X509Certificate
argument_list|>
name|certs
parameter_list|)
block|{
if|if
condition|(
name|certs
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|int
name|size
init|=
name|certs
operator|.
name|size
argument_list|()
decl_stmt|;
name|int
name|counter
init|=
literal|0
decl_stmt|;
for|for
control|(
name|X509Certificate
name|cert
range|:
name|certs
control|)
block|{
name|counter
operator|++
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
expr_stmt|;
name|certToString
argument_list|(
name|sb
argument_list|,
name|cert
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
if|if
condition|(
name|counter
operator|<
name|size
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"; "
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|issuerSerialNumberSubject (X509CertificateHolder cert)
specifier|protected
name|String
name|issuerSerialNumberSubject
parameter_list|(
name|X509CertificateHolder
name|cert
parameter_list|)
block|{
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
literal|"Issuer=("
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|cert
operator|.
name|getIssuer
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"), SerialNumber="
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|cert
operator|.
name|getSerialNumber
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", Subject=("
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|cert
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|certToString (StringBuilder sb, X509Certificate cert)
specifier|protected
name|void
name|certToString
parameter_list|(
name|StringBuilder
name|sb
parameter_list|,
name|X509Certificate
name|cert
parameter_list|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"Issuer=("
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|cert
operator|.
name|getIssuerX500Principal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"), SerialNumber="
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|cert
operator|.
name|getSerialNumber
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", Subject=("
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|cert
operator|.
name|getSubjectX500Principal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

