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
name|util
operator|.
name|LinkedList
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
name|ProducerTemplate
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
name|main
operator|.
name|MainSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_comment
comment|/**  * A command line tool for booting up a CamelContext using an OSGi Blueprint XML file  */
end_comment

begin_class
DECL|class|Main
specifier|public
class|class
name|Main
extends|extends
name|MainSupport
block|{
DECL|field|instance
specifier|protected
specifier|static
name|Main
name|instance
decl_stmt|;
DECL|field|bundleContext
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
DECL|field|descriptors
specifier|private
name|String
name|descriptors
init|=
literal|"OSGI-INF/blueprint/*.xml"
decl_stmt|;
DECL|field|bundleName
specifier|private
name|String
name|bundleName
init|=
literal|"MyBundle"
decl_stmt|;
DECL|field|includeSelfAsBundle
specifier|private
name|boolean
name|includeSelfAsBundle
decl_stmt|;
DECL|field|configAdminPid
specifier|private
name|String
name|configAdminPid
decl_stmt|;
DECL|field|configAdminFileName
specifier|private
name|String
name|configAdminFileName
decl_stmt|;
comment|// ClassLoader used to scan for bundles in CamelBlueprintHelper.createBundleContext()
DECL|field|loader
specifier|private
name|ClassLoader
name|loader
decl_stmt|;
DECL|method|Main ()
specifier|public
name|Main
parameter_list|()
block|{
name|addOption
argument_list|(
operator|new
name|ParameterOption
argument_list|(
literal|"ac"
argument_list|,
literal|"applicationContext"
argument_list|,
literal|"Sets the classpath based OSGi Blueprint"
argument_list|,
literal|"applicationContext"
argument_list|)
block|{
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|String
name|parameter
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
name|setDescriptors
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|addOption
argument_list|(
operator|new
name|ParameterOption
argument_list|(
literal|"fa"
argument_list|,
literal|"fileApplicationContext"
argument_list|,
literal|"Sets the filesystem based OSGi Blueprint"
argument_list|,
literal|"fileApplicationContext"
argument_list|)
block|{
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|String
name|parameter
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
name|setDescriptors
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|addOption
argument_list|(
operator|new
name|ParameterOption
argument_list|(
literal|"pid"
argument_list|,
literal|"configAdminPid"
argument_list|,
literal|"Sets the ConfigAdmin persistentId"
argument_list|,
literal|"configAdminPid"
argument_list|)
block|{
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|String
name|parameter
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
name|setConfigAdminPid
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|addOption
argument_list|(
operator|new
name|ParameterOption
argument_list|(
literal|"pf"
argument_list|,
literal|"configAdminFileName"
argument_list|,
literal|"Sets the ConfigAdmin persistent file name"
argument_list|,
literal|"configAdminFileName"
argument_list|)
block|{
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|String
name|parameter
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
name|setConfigAdminFileName
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|main (String... args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
modifier|...
name|args
parameter_list|)
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
name|run
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the currently executing main      *      * @return the current running instance      */
DECL|method|getInstance ()
specifier|public
specifier|static
name|Main
name|getInstance
parameter_list|()
block|{
return|return
name|instance
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
block|{
return|return
name|CamelBlueprintHelper
operator|.
name|getOsgiService
argument_list|(
name|bundleContext
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|bundleContext
operator|==
literal|null
condition|)
block|{
name|String
name|descriptors
init|=
name|getDescriptors
argument_list|()
decl_stmt|;
if|if
condition|(
name|descriptors
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Descriptors must be provided, with the name of the blueprint XML file"
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Starting Blueprint XML file: "
operator|+
name|descriptors
argument_list|)
expr_stmt|;
if|if
condition|(
name|configAdminPid
operator|!=
literal|null
operator|&&
name|configAdminFileName
operator|!=
literal|null
condition|)
block|{
comment|// pid/file is used to set INITIAL content of ConfigAdmin to be used when blueprint container is started
name|bundleContext
operator|=
name|createBundleContext
argument_list|(
name|bundleName
argument_list|,
operator|new
name|String
index|[]
block|{
name|configAdminFileName
block|,
name|configAdminPid
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bundleContext
operator|=
name|createBundleContext
argument_list|(
name|bundleName
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|initCamelContext
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
comment|// if we were veto started then mark as completed
if|if
condition|(
name|getCamelContext
argument_list|()
operator|!=
literal|null
operator|&&
name|getCamelContext
argument_list|()
operator|.
name|isVetoStarted
argument_list|()
condition|)
block|{
name|completed
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// stop camel context
if|if
condition|(
name|getCamelContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getCamelContext
argument_list|()
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
comment|// and then stop blueprint
name|LOG
operator|.
name|debug
argument_list|(
literal|"Stopping Blueprint XML file: {}"
argument_list|,
name|descriptors
argument_list|)
expr_stmt|;
name|CamelBlueprintHelper
operator|.
name|disposeBundleContext
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
comment|// call completed to properly stop as we count down the waiting latch
name|completed
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|findOrCreateCamelTemplate ()
specifier|protected
name|ProducerTemplate
name|findOrCreateCamelTemplate
parameter_list|()
block|{
if|if
condition|(
name|getCamelContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getCamelContext
argument_list|()
operator|.
name|createProducerTemplate
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|createBundleContext ()
specifier|protected
name|BundleContext
name|createBundleContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|createBundleContext
argument_list|(
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createBundleContext (String name, String[]... configAdminPidFiles)
specifier|protected
name|BundleContext
name|createBundleContext
parameter_list|(
name|String
name|name
parameter_list|,
name|String
index|[]
modifier|...
name|configAdminPidFiles
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createBundleContext
argument_list|(
name|name
argument_list|,
name|loader
argument_list|,
name|configAdminPidFiles
argument_list|)
return|;
block|}
DECL|method|createBundleContext (String name, ClassLoader loader, String[]... configAdminPidFiles)
specifier|protected
name|BundleContext
name|createBundleContext
parameter_list|(
name|String
name|name
parameter_list|,
name|ClassLoader
name|loader
parameter_list|,
name|String
index|[]
modifier|...
name|configAdminPidFiles
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|CamelBlueprintHelper
operator|.
name|createBundleContext
argument_list|(
name|name
argument_list|,
name|descriptors
argument_list|,
name|isIncludeSelfAsBundle
argument_list|()
argument_list|,
name|CamelBlueprintHelper
operator|.
name|BUNDLE_FILTER
argument_list|,
name|CamelBlueprintHelper
operator|.
name|BUNDLE_VERSION
argument_list|,
literal|null
argument_list|,
name|loader
argument_list|,
name|configAdminPidFiles
argument_list|)
return|;
block|}
DECL|method|getDescriptors ()
specifier|public
name|String
name|getDescriptors
parameter_list|()
block|{
return|return
name|descriptors
return|;
block|}
DECL|method|setDescriptors (String descriptors)
specifier|public
name|void
name|setDescriptors
parameter_list|(
name|String
name|descriptors
parameter_list|)
block|{
name|this
operator|.
name|descriptors
operator|=
name|descriptors
expr_stmt|;
block|}
DECL|method|getBundleName ()
specifier|public
name|String
name|getBundleName
parameter_list|()
block|{
return|return
name|bundleName
return|;
block|}
DECL|method|setBundleName (String bundleName)
specifier|public
name|void
name|setBundleName
parameter_list|(
name|String
name|bundleName
parameter_list|)
block|{
name|this
operator|.
name|bundleName
operator|=
name|bundleName
expr_stmt|;
block|}
DECL|method|isIncludeSelfAsBundle ()
specifier|public
name|boolean
name|isIncludeSelfAsBundle
parameter_list|()
block|{
return|return
name|includeSelfAsBundle
return|;
block|}
DECL|method|setIncludeSelfAsBundle (boolean includeSelfAsBundle)
specifier|public
name|void
name|setIncludeSelfAsBundle
parameter_list|(
name|boolean
name|includeSelfAsBundle
parameter_list|)
block|{
name|this
operator|.
name|includeSelfAsBundle
operator|=
name|includeSelfAsBundle
expr_stmt|;
block|}
DECL|method|getConfigAdminPid ()
specifier|public
name|String
name|getConfigAdminPid
parameter_list|()
block|{
return|return
name|configAdminPid
return|;
block|}
DECL|method|setConfigAdminPid (String pid)
specifier|public
name|void
name|setConfigAdminPid
parameter_list|(
name|String
name|pid
parameter_list|)
block|{
name|this
operator|.
name|configAdminPid
operator|=
name|pid
expr_stmt|;
block|}
DECL|method|getConfigAdminFileName ()
specifier|public
name|String
name|getConfigAdminFileName
parameter_list|()
block|{
return|return
name|configAdminFileName
return|;
block|}
DECL|method|setConfigAdminFileName (String fileName)
specifier|public
name|void
name|setConfigAdminFileName
parameter_list|(
name|String
name|fileName
parameter_list|)
block|{
name|this
operator|.
name|configAdminFileName
operator|=
name|fileName
expr_stmt|;
block|}
DECL|method|setLoader (ClassLoader loader)
specifier|public
name|void
name|setLoader
parameter_list|(
name|ClassLoader
name|loader
parameter_list|)
block|{
name|this
operator|.
name|loader
operator|=
name|loader
expr_stmt|;
block|}
block|}
end_class

end_unit

