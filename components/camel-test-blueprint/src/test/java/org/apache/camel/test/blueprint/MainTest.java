begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|net
operator|.
name|URLClassLoader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|util
operator|.
name|io
operator|.
name|IOUtils
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
name|ProducerTemplate
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
name|ops4j
operator|.
name|pax
operator|.
name|swissbox
operator|.
name|tinybundles
operator|.
name|core
operator|.
name|TinyBundle
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
name|swissbox
operator|.
name|tinybundles
operator|.
name|core
operator|.
name|TinyBundles
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_class
DECL|class|MainTest
specifier|public
class|class
name|MainTest
block|{
DECL|field|SYMBOLIC_NAME
specifier|private
specifier|static
specifier|final
name|String
name|SYMBOLIC_NAME
init|=
literal|"testMainWithoutIncludingTestBundle"
decl_stmt|;
annotation|@
name|Test
DECL|method|testMyMain ()
specifier|public
name|void
name|testMyMain
parameter_list|()
throws|throws
name|Exception
block|{
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|main
operator|.
name|setBundleName
argument_list|(
literal|"MyMainBundle"
argument_list|)
expr_stmt|;
comment|// as we run this test without packing ourselves as bundle, then include ourselves
name|main
operator|.
name|setIncludeSelfAsBundle
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// setup the blueprint file here
name|main
operator|.
name|setDescriptors
argument_list|(
literal|"org/apache/camel/test/blueprint/main-loadfile.xml"
argument_list|)
expr_stmt|;
comment|// set the configAdmin persistent id
name|main
operator|.
name|setConfigAdminPid
argument_list|(
literal|"stuff"
argument_list|)
expr_stmt|;
comment|// set the configAdmin persistent file name
name|main
operator|.
name|setConfigAdminFileName
argument_list|(
literal|"src/test/resources/etc/stuff.cfg"
argument_list|)
expr_stmt|;
name|main
operator|.
name|start
argument_list|()
expr_stmt|;
name|ProducerTemplate
name|template
init|=
name|main
operator|.
name|getCamelTemplate
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"We should get the template here"
argument_list|,
name|template
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"hello"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response"
argument_list|,
literal|"Bye hello"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|main
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMainWithoutIncludingTestBundle ()
specifier|public
name|void
name|testMainWithoutIncludingTestBundle
parameter_list|()
throws|throws
name|Exception
block|{
name|TinyBundle
name|bundle
init|=
name|TinyBundles
operator|.
name|newBundle
argument_list|()
decl_stmt|;
name|bundle
operator|.
name|add
argument_list|(
literal|"OSGI-INF/blueprint/camel.xml"
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"main-loadfile.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|bundle
operator|.
name|set
argument_list|(
literal|"Manifest-Version"
argument_list|,
literal|"2"
argument_list|)
operator|.
name|set
argument_list|(
literal|"Bundle-ManifestVersion"
argument_list|,
literal|"2"
argument_list|)
operator|.
name|set
argument_list|(
literal|"Bundle-SymbolicName"
argument_list|,
name|SYMBOLIC_NAME
argument_list|)
operator|.
name|set
argument_list|(
literal|"Bundle-Version"
argument_list|,
literal|"1.0.0"
argument_list|)
expr_stmt|;
name|File
name|tb
init|=
name|File
operator|.
name|createTempFile
argument_list|(
name|SYMBOLIC_NAME
operator|+
literal|"-"
argument_list|,
literal|".jar"
argument_list|,
operator|new
name|File
argument_list|(
literal|"target"
argument_list|)
argument_list|)
decl_stmt|;
name|FileOutputStream
name|out
init|=
operator|new
name|FileOutputStream
argument_list|(
name|tb
argument_list|)
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|bundle
operator|.
name|build
argument_list|()
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// simulate `camel:run` which is run after packaging the artifact, so a "bundle" (location with
comment|// META-INF/MANIFEST.MF) is detected in target/classes
name|URLClassLoader
name|loader
init|=
operator|new
name|URLClassLoader
argument_list|(
operator|new
name|URL
index|[]
block|{
name|tb
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
block|}
argument_list|,
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|main
operator|.
name|setLoader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
comment|// bundle name will be used as filter for blueprint container filter
name|main
operator|.
name|setBundleName
argument_list|(
name|SYMBOLIC_NAME
argument_list|)
expr_stmt|;
comment|// don't include test bundle (which is what `mvn camel:run` actually does)
name|main
operator|.
name|setIncludeSelfAsBundle
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// don't setup the blueprint file here - it'll be picked up from a bundle on classpath
comment|//main.setDescriptors("none!");
comment|// set the configAdmin persistent id
name|main
operator|.
name|setConfigAdminPid
argument_list|(
literal|"stuff"
argument_list|)
expr_stmt|;
comment|// set the configAdmin persistent file name
name|main
operator|.
name|setConfigAdminFileName
argument_list|(
literal|"src/test/resources/etc/stuff.cfg"
argument_list|)
expr_stmt|;
name|main
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|ProducerTemplate
name|template
init|=
name|main
operator|.
name|getCamelTemplate
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"We should get the template here"
argument_list|,
name|template
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"hello"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response"
argument_list|,
literal|"Bye hello"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|main
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

