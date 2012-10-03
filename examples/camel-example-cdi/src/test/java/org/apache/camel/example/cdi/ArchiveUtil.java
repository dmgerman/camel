begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.example.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cdi
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
name|cdi
operator|.
name|CdiCamelContext
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
name|cdi
operator|.
name|internal
operator|.
name|CamelExtension
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|arquillian
operator|.
name|container
operator|.
name|test
operator|.
name|api
operator|.
name|TargetsContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|Archive
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|ShrinkWrap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|asset
operator|.
name|EmptyAsset
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|spec
operator|.
name|JavaArchive
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|spec
operator|.
name|WebArchive
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|resolver
operator|.
name|api
operator|.
name|maven
operator|.
name|Maven
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

begin_comment
comment|/**  *  Util class used to create Jar or War archive used by Arquillian  */
end_comment

begin_class
DECL|class|ArchiveUtil
specifier|public
class|class
name|ArchiveUtil
block|{
annotation|@
name|TargetsContainer
argument_list|(
literal|""
argument_list|)
DECL|method|createJarArchive (String[] packages)
specifier|public
specifier|static
name|Archive
argument_list|<
name|?
argument_list|>
name|createJarArchive
parameter_list|(
name|String
index|[]
name|packages
parameter_list|)
block|{
name|JavaArchive
name|jar
init|=
name|ShrinkWrap
operator|.
name|create
argument_list|(
name|JavaArchive
operator|.
name|class
argument_list|)
operator|.
name|addPackage
argument_list|(
name|CdiCamelContext
operator|.
name|class
operator|.
name|getPackage
argument_list|()
argument_list|)
operator|.
name|addPackage
argument_list|(
name|CamelExtension
operator|.
name|class
operator|.
name|getPackage
argument_list|()
argument_list|)
operator|.
name|addPackages
argument_list|(
literal|false
argument_list|,
name|packages
argument_list|)
operator|.
name|addAsManifestResource
argument_list|(
name|EmptyAsset
operator|.
name|INSTANCE
argument_list|,
literal|"beans.xml"
argument_list|)
decl_stmt|;
comment|// System.out.println(jar.toString(true));
return|return
name|jar
return|;
block|}
annotation|@
name|TargetsContainer
argument_list|(
literal|""
argument_list|)
DECL|method|createWarArchive (String[] packages)
specifier|public
specifier|static
name|Archive
argument_list|<
name|?
argument_list|>
name|createWarArchive
parameter_list|(
name|String
index|[]
name|packages
parameter_list|)
block|{
name|JavaArchive
name|jarTest
init|=
name|ShrinkWrap
operator|.
name|create
argument_list|(
name|JavaArchive
operator|.
name|class
argument_list|)
operator|.
name|addPackages
argument_list|(
literal|false
argument_list|,
name|packages
argument_list|)
operator|.
name|addAsManifestResource
argument_list|(
name|EmptyAsset
operator|.
name|INSTANCE
argument_list|,
literal|"beans.xml"
argument_list|)
decl_stmt|;
name|File
index|[]
name|libs
init|=
name|Maven
operator|.
name|resolver
argument_list|()
operator|.
name|loadPomFromFile
argument_list|(
literal|"pom.xml"
argument_list|)
operator|.
name|resolve
argument_list|(
literal|"org.apache.camel:camel-core"
argument_list|,
literal|"org.apache.camel:camel-cdi"
argument_list|,
literal|"org.apache.activemq:activemq-camel"
argument_list|)
operator|.
name|withTransitivity
argument_list|()
operator|.
name|as
argument_list|(
name|File
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|ShrinkWrap
operator|.
name|create
argument_list|(
name|WebArchive
operator|.
name|class
argument_list|,
literal|"test.war"
argument_list|)
operator|.
name|addAsLibrary
argument_list|(
name|jarTest
argument_list|)
operator|.
name|addAsLibraries
argument_list|(
name|libs
argument_list|)
return|;
block|}
block|}
end_class

end_unit

