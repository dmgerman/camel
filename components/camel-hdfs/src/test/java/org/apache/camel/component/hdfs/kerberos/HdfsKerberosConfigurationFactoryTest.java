begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.hdfs.kerberos
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hdfs
operator|.
name|kerberos
package|;
end_package

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
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
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

begin_class
DECL|class|HdfsKerberosConfigurationFactoryTest
specifier|public
class|class
name|HdfsKerberosConfigurationFactoryTest
block|{
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|FileNotFoundException
operator|.
name|class
argument_list|)
DECL|method|setupExistingKerberosConfigFileWithMissingConfigFile ()
specifier|public
name|void
name|setupExistingKerberosConfigFileWithMissingConfigFile
parameter_list|()
throws|throws
name|IOException
block|{
comment|// given
name|String
name|kerberosConfigFileLocation
init|=
literal|"missing.conf"
decl_stmt|;
comment|// when
name|HdfsKerberosConfigurationFactory
operator|.
name|setKerberosConfigFile
argument_list|(
name|kerberosConfigFileLocation
argument_list|)
expr_stmt|;
comment|// then
comment|/* exception was thrown */
block|}
block|}
end_class

end_unit

