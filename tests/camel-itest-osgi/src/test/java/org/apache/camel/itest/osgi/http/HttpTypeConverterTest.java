begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.http
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
name|http
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
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletOutputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|Cookie
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|impl
operator|.
name|DefaultMessage
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
name|itest
operator|.
name|osgi
operator|.
name|OSGiIntegrationTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|JUnit4TestRunner
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|OptionUtils
operator|.
name|combine
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4TestRunner
operator|.
name|class
argument_list|)
DECL|class|HttpTypeConverterTest
specifier|public
class|class
name|HttpTypeConverterTest
extends|extends
name|OSGiIntegrationTestSupport
block|{
DECL|field|servletResponse
specifier|static
name|HttpServletResponse
name|servletResponse
init|=
operator|new
name|HttpServletResponse
argument_list|()
block|{
specifier|public
name|void
name|addCookie
parameter_list|(
name|Cookie
name|cookie
parameter_list|)
block|{         }
specifier|public
name|void
name|addDateHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|long
name|date
parameter_list|)
block|{         }
specifier|public
name|void
name|addHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{         }
specifier|public
name|void
name|addIntHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|value
parameter_list|)
block|{         }
specifier|public
name|boolean
name|containsHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|String
name|encodeRedirectURL
parameter_list|(
name|String
name|url
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|encodeRedirectUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|encodeURL
parameter_list|(
name|String
name|url
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|encodeUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|sendError
parameter_list|(
name|int
name|sc
parameter_list|)
throws|throws
name|IOException
block|{         }
specifier|public
name|void
name|sendError
parameter_list|(
name|int
name|sc
parameter_list|,
name|String
name|msg
parameter_list|)
throws|throws
name|IOException
block|{         }
specifier|public
name|void
name|sendRedirect
parameter_list|(
name|String
name|location
parameter_list|)
throws|throws
name|IOException
block|{         }
specifier|public
name|void
name|setDateHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|long
name|date
parameter_list|)
block|{         }
specifier|public
name|void
name|setHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{         }
specifier|public
name|void
name|setIntHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|value
parameter_list|)
block|{         }
specifier|public
name|void
name|setStatus
parameter_list|(
name|int
name|sc
parameter_list|)
block|{         }
specifier|public
name|void
name|setStatus
parameter_list|(
name|int
name|sc
parameter_list|,
name|String
name|sm
parameter_list|)
block|{         }
specifier|public
name|void
name|flushBuffer
parameter_list|()
throws|throws
name|IOException
block|{         }
specifier|public
name|int
name|getBufferSize
parameter_list|()
block|{
return|return
literal|1024
return|;
block|}
specifier|public
name|String
name|getCharacterEncoding
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Locale
name|getLocale
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|ServletOutputStream
name|getOutputStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|PrintWriter
name|getWriter
parameter_list|()
throws|throws
name|IOException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isCommitted
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|void
name|reset
parameter_list|()
block|{         }
specifier|public
name|void
name|resetBuffer
parameter_list|()
block|{         }
specifier|public
name|void
name|setBufferSize
parameter_list|(
name|int
name|size
parameter_list|)
block|{         }
specifier|public
name|void
name|setCharacterEncoding
parameter_list|(
name|String
name|charset
parameter_list|)
block|{         }
specifier|public
name|void
name|setContentLength
parameter_list|(
name|int
name|len
parameter_list|)
block|{         }
specifier|public
name|void
name|setContentType
parameter_list|(
name|String
name|type
parameter_list|)
block|{         }
specifier|public
name|void
name|setLocale
parameter_list|(
name|Locale
name|loc
parameter_list|)
block|{         }
block|}
decl_stmt|;
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testHttpConverter ()
specifier|public
name|void
name|testHttpConverter
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|message
init|=
operator|new
name|DefaultMessage
argument_list|()
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_SERVLET_RESPONSE
argument_list|,
name|servletResponse
argument_list|)
expr_stmt|;
name|HttpServletResponse
name|result
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|HttpServletResponse
operator|.
name|class
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
literal|"The http conveter doesn't work"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Configuration
DECL|method|configure ()
specifier|public
specifier|static
name|Option
index|[]
name|configure
parameter_list|()
block|{
name|Option
index|[]
name|options
init|=
name|combine
argument_list|(
name|getDefaultCamelKarafOptions
argument_list|()
argument_list|,
comment|// using the features to install the other camel components
name|loadCamelFeatures
argument_list|(
literal|"camel-http"
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|options
return|;
block|}
block|}
end_class

end_unit

