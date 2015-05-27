begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ahc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ahc
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
name|impl
operator|.
name|DefaultHeaderFilterStrategy
import|;
end_import

begin_class
DECL|class|HttpProtocolHeaderFilterStrategy
specifier|public
class|class
name|HttpProtocolHeaderFilterStrategy
extends|extends
name|DefaultHeaderFilterStrategy
block|{
DECL|method|HttpProtocolHeaderFilterStrategy ()
specifier|public
name|HttpProtocolHeaderFilterStrategy
parameter_list|()
block|{
name|initialize
argument_list|()
expr_stmt|;
block|}
comment|// Just add the http headers here
DECL|method|initialize ()
specifier|protected
name|void
name|initialize
parameter_list|()
block|{
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"host"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"content-encoding"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"content-language"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"content-location"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"content-md5"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"content-length"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"content-type"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"content-range"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"dav"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"depth"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"destination"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"etag"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"expect"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"expires"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"from"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"if"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"if-match"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"if-modified-since"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"if-none-match"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"if-range"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"if-unmodified-since"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"last-modified"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"location"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"lock-token"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"max-forwards"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"overwrite"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"pragma"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"proxy-authenticate"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"proxy-authorization"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"range"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"referer"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"retry-after"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"server"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"status-uri"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"te"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"timeout"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"user-agent"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"vary"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"www-authenticate"
argument_list|)
expr_stmt|;
comment|// Add the filter for the Generic Message header
comment|// http://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.5
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"cache-control"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"connection"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"date"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"pragma"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"trailer"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"transfer-encoding"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"upgrade"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"via"
argument_list|)
expr_stmt|;
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"warning"
argument_list|)
expr_stmt|;
name|setLowerCase
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

