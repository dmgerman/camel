begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
operator|.
name|blueprint
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
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
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
name|jsse
operator|.
name|SSLContextParameters
import|;
end_import

begin_class
DECL|class|JsseUtilTester
specifier|public
class|class
name|JsseUtilTester
block|{
DECL|field|context
specifier|private
name|SSLContext
name|context
decl_stmt|;
DECL|method|JsseUtilTester (SSLContextParameters params)
specifier|public
name|JsseUtilTester
parameter_list|(
name|SSLContextParameters
name|params
parameter_list|)
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
name|context
operator|=
name|params
operator|.
name|createSSLContext
argument_list|()
expr_stmt|;
block|}
DECL|method|getContext ()
specifier|public
name|SSLContext
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
block|}
end_class

end_unit

