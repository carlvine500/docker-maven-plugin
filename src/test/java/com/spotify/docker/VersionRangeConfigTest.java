package com.spotify.docker;

import com.sun.org.apache.bcel.internal.util.ClassLoader;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.VersionRangeExt;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class VersionRangeConfigTest {

    @Test
    public void check() throws Exception {
        BuildMojo.getVersionRangeExts(ClassLoader.getSystemClassLoader(), "dependency-blacklist.conf");
        BuildMojo.getVersionRangeExts(ClassLoader.getSystemClassLoader(), "dependency-whitelist.conf");
    }

    @Test(expected = MojoExecutionException.class)
    public void checkTry() throws Exception {
        Set<Artifact> set = new HashSet<>();
        set.add(new DefaultArtifact("com.alibaba.rocketmq","rocketmq-client","3.2.6","compile","jar","jdk18",null));
        BuildMojo.checkJarWithBlacklistWhitelist(ClassLoader.getSystemClassLoader(),set);
    }

    @Test
    public void checkInVersionRange()throws Exception{
        VersionRangeExt x = VersionRangeExt.createFromVersionSpec("(,3.2.5),(3.2.5,3.2.6),(3.2.6,)");
        Assert.assertTrue(x.containsVersion(new DefaultArtifactVersion("3.2.4")));
        Assert.assertFalse(x.containsVersion(new DefaultArtifactVersion("3.2.5")));
        Assert.assertTrue(x.containsVersion(new DefaultArtifactVersion("3.2.5.1")));
        Assert.assertFalse(x.containsVersion(new DefaultArtifactVersion("3.2.6")));
        Assert.assertTrue(x.containsVersion(new DefaultArtifactVersion("3.2.7")));
    }

    @Test
    public void checkSameVersionInRange()throws Exception{
        VersionRangeExt x = VersionRangeExt.createFromVersionSpec("[3.2.5,3.2.5]");
        Assert.assertTrue(x.containsVersion(new DefaultArtifactVersion("3.2.5")));
    }
}
