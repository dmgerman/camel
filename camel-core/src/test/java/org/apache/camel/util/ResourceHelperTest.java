begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicReference
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
name|CamelContext
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
name|TestSupport
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
name|converter
operator|.
name|IOConverter
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
name|DefaultCamelContext
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
name|SimpleRegistry
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|ResourceHelperTest
specifier|public
class|class
name|ResourceHelperTest
extends|extends
name|TestSupport
block|{
DECL|method|testLoadFile ()
specifier|public
name|void
name|testLoadFile
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|InputStream
name|is
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|context
argument_list|,
literal|"file:src/test/resources/log4j2.properties"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|text
operator|.
name|contains
argument_list|(
literal|"rootLogger"
argument_list|)
argument_list|)
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testLoadFileWithSpace ()
specifier|public
name|void
name|testLoadFileWithSpace
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|createDirectory
argument_list|(
literal|"target/my space"
argument_list|)
expr_stmt|;
name|FileUtil
operator|.
name|copyFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/log4j2.properties"
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
literal|"target/my space/log4j2.properties"
argument_list|)
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|context
argument_list|,
literal|"file:target/my%20space/log4j2.properties"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|text
operator|.
name|contains
argument_list|(
literal|"rootLogger"
argument_list|)
argument_list|)
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testLoadClasspath ()
specifier|public
name|void
name|testLoadClasspath
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|InputStream
name|is
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|context
argument_list|,
literal|"classpath:log4j2.properties"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|text
operator|.
name|contains
argument_list|(
literal|"rootLogger"
argument_list|)
argument_list|)
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testLoadRegistry ()
specifier|public
name|void
name|testLoadRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"myBean"
argument_list|,
literal|"This is a log4j logging configuration file"
argument_list|)
expr_stmt|;
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|InputStream
name|is
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|context
argument_list|,
literal|"ref:myBean"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|text
operator|.
name|contains
argument_list|(
literal|"log4j"
argument_list|)
argument_list|)
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testLoadBeanDoubleColon ()
specifier|public
name|void
name|testLoadBeanDoubleColon
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"myBean"
argument_list|,
operator|new
name|AtomicReference
argument_list|<
name|InputStream
argument_list|>
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"a"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|InputStream
name|is
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|context
argument_list|,
literal|"bean:myBean::get"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|text
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testLoadBeanDoubleColonLong ()
specifier|public
name|void
name|testLoadBeanDoubleColonLong
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"my.company.MyClass"
argument_list|,
operator|new
name|AtomicReference
argument_list|<
name|InputStream
argument_list|>
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"a"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|InputStream
name|is
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|context
argument_list|,
literal|"bean:my.company.MyClass::get"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|text
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testLoadBeanDot ()
specifier|public
name|void
name|testLoadBeanDot
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"myBean"
argument_list|,
operator|new
name|AtomicReference
argument_list|<
name|InputStream
argument_list|>
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"a"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|InputStream
name|is
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|context
argument_list|,
literal|"bean:myBean.get"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|text
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testLoadClasspathDefault ()
specifier|public
name|void
name|testLoadClasspathDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|InputStream
name|is
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|context
argument_list|,
literal|"log4j2.properties"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|text
operator|.
name|contains
argument_list|(
literal|"rootLogger"
argument_list|)
argument_list|)
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testLoadFileNotFound ()
specifier|public
name|void
name|testLoadFileNotFound
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|context
argument_list|,
literal|"file:src/test/resources/notfound.txt"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should not find file"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"notfound.txt"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testLoadClasspathNotFound ()
specifier|public
name|void
name|testLoadClasspathNotFound
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|context
argument_list|,
literal|"classpath:notfound.txt"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should not find file"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Cannot find resource: classpath:notfound.txt in classpath for URI: classpath:notfound.txt"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testLoadFileAsUrl ()
specifier|public
name|void
name|testLoadFileAsUrl
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|URL
name|url
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsUrl
argument_list|(
name|context
operator|.
name|getClassResolver
argument_list|()
argument_list|,
literal|"file:src/test/resources/log4j2.properties"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|url
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|text
operator|.
name|contains
argument_list|(
literal|"rootLogger"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testLoadClasspathAsUrl ()
specifier|public
name|void
name|testLoadClasspathAsUrl
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|URL
name|url
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsUrl
argument_list|(
name|context
operator|.
name|getClassResolver
argument_list|()
argument_list|,
literal|"classpath:log4j2.properties"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|url
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|text
operator|.
name|contains
argument_list|(
literal|"rootLogger"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testLoadCustomUrlasInputStream ()
specifier|public
name|void
name|testLoadCustomUrlasInputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|String
name|handlerPackageSystemProp
init|=
literal|"java.protocol.handler.pkgs"
decl_stmt|;
name|String
name|customUrlHandlerPackage
init|=
literal|"org.apache.camel.urlhandler"
decl_stmt|;
name|registerSystemProperty
argument_list|(
name|handlerPackageSystemProp
argument_list|,
name|customUrlHandlerPackage
argument_list|,
literal|"|"
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|context
argument_list|,
literal|"custom://hello"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hello"
argument_list|,
name|IOConverter
operator|.
name|toString
argument_list|(
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testLoadCustomUrlasInputStreamFail ()
specifier|public
name|void
name|testLoadCustomUrlasInputStreamFail
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|InputStream
name|is
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|context
argument_list|,
literal|"custom://hello"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"unknown protocol: custom"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testLoadCustomUrl ()
specifier|public
name|void
name|testLoadCustomUrl
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|String
name|handlerPackageSystemProp
init|=
literal|"java.protocol.handler.pkgs"
decl_stmt|;
name|String
name|customUrlHandlerPackage
init|=
literal|"org.apache.camel.urlhandler"
decl_stmt|;
name|registerSystemProperty
argument_list|(
name|handlerPackageSystemProp
argument_list|,
name|customUrlHandlerPackage
argument_list|,
literal|"|"
argument_list|)
expr_stmt|;
name|URL
name|url
init|=
name|ResourceHelper
operator|.
name|resolveResourceAsUrl
argument_list|(
name|context
operator|.
name|getClassResolver
argument_list|()
argument_list|,
literal|"custom://hello"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|url
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|text
operator|.
name|contains
argument_list|(
literal|"hello"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testLoadCustomUrlFail ()
specifier|public
name|void
name|testLoadCustomUrlFail
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|URL
name|url
init|=
name|ResourceHelper
operator|.
name|resolveResourceAsUrl
argument_list|(
name|context
operator|.
name|getClassResolver
argument_list|()
argument_list|,
literal|"custom://hello"
argument_list|)
decl_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"unknown protocol: custom"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testIsHttp ()
specifier|public
name|void
name|testIsHttp
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFalse
argument_list|(
name|ResourceHelper
operator|.
name|isHttpUri
argument_list|(
literal|"direct:foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ResourceHelper
operator|.
name|isHttpUri
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ResourceHelper
operator|.
name|isHttpUri
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ResourceHelper
operator|.
name|isHttpUri
argument_list|(
literal|"http://camel.apache.org"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ResourceHelper
operator|.
name|isHttpUri
argument_list|(
literal|"https://camel.apache.org"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetScheme ()
specifier|public
name|void
name|testGetScheme
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"file:"
argument_list|,
name|ResourceHelper
operator|.
name|getScheme
argument_list|(
literal|"file:myfile.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"classpath:"
argument_list|,
name|ResourceHelper
operator|.
name|getScheme
argument_list|(
literal|"classpath:myfile.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http:"
argument_list|,
name|ResourceHelper
operator|.
name|getScheme
argument_list|(
literal|"http:www.foo.com"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|ResourceHelper
operator|.
name|getScheme
argument_list|(
literal|"www.foo.com"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|ResourceHelper
operator|.
name|getScheme
argument_list|(
literal|"myfile.txt"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testAppendParameters ()
specifier|public
name|void
name|testAppendParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
comment|// should clear the map after usage
name|assertEquals
argument_list|(
literal|"http://localhost:8080/data?foo=123&bar=yes"
argument_list|,
name|ResourceHelper
operator|.
name|appendParameters
argument_list|(
literal|"http://localhost:8080/data"
argument_list|,
name|params
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|params
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

