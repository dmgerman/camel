begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.osgi.activator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|osgi
operator|.
name|activator
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
name|IOException
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
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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
name|karaf
operator|.
name|options
operator|.
name|LogLevelOption
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
name|options
operator|.
name|DefaultCompositeOption
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
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|maven
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
name|CoreOptions
operator|.
name|streamBundle
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
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|configureConsole
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
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|karafDistributionConfiguration
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
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|keepRuntimeFolder
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
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|logLevel
import|;
end_import

begin_enum
DECL|enum|PaxExamOptions
specifier|public
enum|enum
name|PaxExamOptions
block|{
DECL|enumConstant|KARAF
name|KARAF
argument_list|(
DECL|enumConstant|karafDistributionConfiguration
name|karafDistributionConfiguration
argument_list|()
DECL|enumConstant|frameworkUrl
operator|.
name|frameworkUrl
argument_list|(
name|maven
argument_list|()
DECL|enumConstant|groupId
operator|.
name|groupId
argument_list|(
literal|"org.apache.karaf"
argument_list|)
DECL|enumConstant|artifactId
operator|.
name|artifactId
argument_list|(
literal|"apache-karaf"
argument_list|)
DECL|enumConstant|versionAsInProject
operator|.
name|versionAsInProject
argument_list|()
DECL|enumConstant|type
operator|.
name|type
argument_list|(
literal|"zip"
argument_list|)
argument_list|)
DECL|enumConstant|name
operator|.
name|name
argument_list|(
literal|"Apache Karaf"
argument_list|)
DECL|enumConstant|useDeployFolder
operator|.
name|useDeployFolder
argument_list|(
literal|false
argument_list|)
DECL|enumConstant|unpackDirectory
operator|.
name|unpackDirectory
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/paxexam/unpack/"
argument_list|)
argument_list|)
argument_list|,
DECL|enumConstant|keepRuntimeFolder
name|keepRuntimeFolder
argument_list|()
argument_list|,
comment|// Don't bother with local console output as it just ends up cluttering the logs
DECL|enumConstant|configureConsole
DECL|enumConstant|ignoreLocalConsole
name|configureConsole
argument_list|()
operator|.
name|ignoreLocalConsole
argument_list|()
argument_list|,
comment|// Force the log level to INFO so we have more details during the test. It defaults to WARN.
DECL|enumConstant|logLevel
name|logLevel
argument_list|(
name|LogLevelOption
operator|.
name|LogLevel
operator|.
name|INFO
argument_list|)
argument_list|)
block|,
DECL|enumConstant|CAMEL_CORE_OSGI
name|CAMEL_CORE_OSGI
argument_list|(
DECL|enumConstant|createStreamBundleOption
name|createStreamBundleOption
argument_list|(
literal|"camel-core-engine.jar"
argument_list|)
argument_list|,
DECL|enumConstant|createStreamBundleOption
name|createStreamBundleOption
argument_list|(
literal|"camel-api.jar"
argument_list|)
argument_list|,
DECL|enumConstant|createStreamBundleOption
name|createStreamBundleOption
argument_list|(
literal|"camel-base.jar"
argument_list|)
argument_list|,
DECL|enumConstant|createStreamBundleOption
name|createStreamBundleOption
argument_list|(
literal|"camel-jaxp.jar"
argument_list|)
argument_list|,
DECL|enumConstant|createStreamBundleOption
name|createStreamBundleOption
argument_list|(
literal|"camel-management-api.jar"
argument_list|)
argument_list|,
DECL|enumConstant|createStreamBundleOption
name|createStreamBundleOption
argument_list|(
literal|"camel-support.jar"
argument_list|)
argument_list|,
DECL|enumConstant|createStreamBundleOption
name|createStreamBundleOption
argument_list|(
literal|"camel-util.jar"
argument_list|)
argument_list|,
DECL|enumConstant|createStreamBundleOption
name|createStreamBundleOption
argument_list|(
literal|"camel-util-json.jar"
argument_list|)
argument_list|,
DECL|enumConstant|createStreamBundleOption
name|createStreamBundleOption
argument_list|(
literal|"spi-annotations.jar"
argument_list|)
argument_list|,
DECL|enumConstant|createStreamBundleOption
name|createStreamBundleOption
argument_list|(
literal|"camel-timer.jar"
argument_list|)
argument_list|,
DECL|enumConstant|createStreamBundleOption
name|createStreamBundleOption
argument_list|(
literal|"camel-log.jar"
argument_list|)
argument_list|,
DECL|enumConstant|createStreamBundleOption
name|createStreamBundleOption
argument_list|(
literal|"camel-core-osgi.jar"
argument_list|)
argument_list|)
block|;
DECL|field|options
specifier|private
specifier|final
name|Option
index|[]
name|options
decl_stmt|;
DECL|method|PaxExamOptions (Option... options)
name|PaxExamOptions
parameter_list|(
name|Option
modifier|...
name|options
parameter_list|)
block|{
name|this
operator|.
name|options
operator|=
name|options
expr_stmt|;
block|}
DECL|method|option ()
specifier|public
name|Option
name|option
parameter_list|()
block|{
return|return
operator|new
name|DefaultCompositeOption
argument_list|(
name|options
argument_list|)
return|;
block|}
DECL|method|createStreamBundleOption (String fileName)
specifier|public
specifier|static
name|Option
name|createStreamBundleOption
parameter_list|(
name|String
name|fileName
parameter_list|)
block|{
name|InputStream
name|bundleInputStream
init|=
literal|null
decl_stmt|;
try|try
block|{
name|bundleInputStream
operator|=
name|Files
operator|.
name|newInputStream
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"target/test-bundles"
argument_list|)
operator|.
name|resolve
argument_list|(
name|fileName
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error resolving Bundle"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|streamBundle
argument_list|(
name|TinyBundles
operator|.
name|bundle
argument_list|()
operator|.
name|read
argument_list|(
name|bundleInputStream
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
block|}
end_enum

end_unit

