begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|api
operator|.
name|management
operator|.
name|ManagedCamelContext
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedCamelContextMBean
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Helper to dump route coverage when using {@link EnableRouteCoverage}.  */
end_comment

begin_class
DECL|class|RouteCoverageDumper
specifier|public
specifier|final
class|class
name|RouteCoverageDumper
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RouteCoverageDumper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|RouteCoverageDumper ()
specifier|private
name|RouteCoverageDumper
parameter_list|()
block|{     }
DECL|method|dumpRouteCoverage (CamelContext context, String testClassName, String testName)
specifier|public
specifier|static
name|void
name|dumpRouteCoverage
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|testClassName
parameter_list|,
name|String
name|testName
parameter_list|)
block|{
try|try
block|{
name|String
name|dir
init|=
literal|"target/camel-route-coverage"
decl_stmt|;
name|String
name|name
init|=
name|testClassName
operator|+
literal|"-"
operator|+
name|testName
operator|+
literal|".xml"
decl_stmt|;
name|ManagedCamelContextMBean
name|managedCamelContext
init|=
name|context
operator|.
name|getExtension
argument_list|(
name|ManagedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getManagedCamelContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|managedCamelContext
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Cannot dump route coverage to file as JMX is not enabled. Override useJmx() method to enable JMX in the unit test classes."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|xml
init|=
name|managedCamelContext
operator|.
name|dumpRoutesCoverageAsXml
argument_list|()
decl_stmt|;
name|String
name|combined
init|=
literal|"<camelRouteCoverage>\n"
operator|+
name|gatherTestDetailsAsXml
argument_list|(
name|testClassName
argument_list|,
name|testName
argument_list|)
operator|+
name|xml
operator|+
literal|"\n</camelRouteCoverage>"
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|dir
argument_list|)
decl_stmt|;
comment|// ensure dir exists
name|file
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|file
operator|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Dumping route coverage to file: "
operator|+
name|file
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|combined
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|OutputStream
name|os
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|IOHelper
operator|.
name|copyAndCloseInput
argument_list|(
name|is
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|os
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error during dumping route coverage statistic. This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Gathers test details as xml      */
DECL|method|gatherTestDetailsAsXml (String testClassName, String testName)
specifier|private
specifier|static
name|String
name|gatherTestDetailsAsXml
parameter_list|(
name|String
name|testClassName
parameter_list|,
name|String
name|testName
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
literal|"<test>\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<class>"
argument_list|)
operator|.
name|append
argument_list|(
name|testClassName
argument_list|)
operator|.
name|append
argument_list|(
literal|"</class>\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<method>"
argument_list|)
operator|.
name|append
argument_list|(
name|testName
argument_list|)
operator|.
name|append
argument_list|(
literal|"</method>\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"</test>\n"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

